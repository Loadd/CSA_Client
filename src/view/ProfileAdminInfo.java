package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Account;
import model.ServerConnection;

import java.util.ArrayList;

public class ProfileAdminInfo extends VBox {
    private Label username;
    private TextField money, findUsername;
    private Button update, promote, demote, ban, unban, find, reset;
    private ArrayList<HBox> records;
    private ServerConnection server;

    public ProfileAdminInfo(ServerConnection server){
        records = new ArrayList<>();
        this.server = server;

    }

    public void populateTable(ArrayList<Account> accounts){
        records.clear();
        VBox vbox = new VBox();
        vbox.setSpacing(2);

        for(Account account: accounts){
            HBox record = new HBox();
            record.setSpacing(5);
            record.setStyle("-fx-background-color: transparent");

            username = new Label(account.getUsername());
            money = new TextField(account.getMoney().toString());
            update = new Button("Update");
            promote = new Button("Promote");
            demote = new Button("Demote");
            ban = new Button("Ban");
            unban = new Button("Unban");

            username.setStyle("-fx-font-size: 15");
            update.setStyle("-fx-font-size: 10");
            promote.setStyle("-fx-font-size: 10");
            demote.setStyle("-fx-font-size: 10");
            ban.setStyle("-fx-font-size: 10");
            unban.setStyle("-fx-font-size: 10");

            update.setOnAction(new CreateProfileAdminUpdateHandler(record));
            promote.setOnAction(new CreateProfileAdminPromoteHandler(record));
            demote.setOnAction(new CreateProfileAdminDemoteHandler(record));
            ban.setOnAction(new CreateProfileAdminBanHandler(record));
            unban.setOnAction(new CreateProfileAdminUnbanHandler(record));

            if(account.getAccountType().equals("User") || account.getAccountType().equals("Admin")){
                demote.setVisible(false);
                demote.managedProperty().bind(demote.visibleProperty());

            }

            if(account.getAccountType().equals("Admin")){
                promote.setVisible(false);
                promote.managedProperty().bind(promote.visibleProperty());
            }

            if(account.getBan().equals(true)){
                ban.setVisible(false);
                ban.managedProperty().bind(ban.visibleProperty());

            } else {
                unban.setVisible(false);
                unban.managedProperty().bind(unban.visibleProperty());
            }

            record.getChildren().addAll(username, money, update, promote, demote, ban, unban);
            records.add(record);

        }

        for (HBox record: records){
            vbox.getChildren().add(record);

        }
        HBox record = new HBox();

        findUsername = new TextField("Type a Username");
        find = new Button("Find");
        reset = new Button("Reset");

        find.setOnAction(new CreateProfileAdminFindHandler());
        reset.setOnAction(new CreateProfileAdminResetHandler());

        record.setAlignment(Pos.CENTER);
        record.getChildren().addAll(findUsername, find, reset);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(vbox);
        scroll.setMinWidth(350);
        scroll.setMaxHeight(1000);
        scroll.setStyle("-fx-background-color: transparent");

        this.setSpacing(5);
        this.setStyle("-fx-background-color: rgb(30,30,30); -fx-background-radius: 2");
        this.getChildren().setAll(scroll, record);
        
    }

    private class CreateProfileAdminUpdateHandler implements EventHandler<ActionEvent>{
        private HBox record;
        public CreateProfileAdminUpdateHandler(HBox i){record = i;}

        public void handle(ActionEvent e){
            String username = ((Label) record.getChildren().get(0)).getText();
            String money = ((TextField) record.getChildren().get(1)).getText();
            Account account = server.getAccount(username);
            server.updateProfileMoney(account, Float.valueOf(money));

        }
    }

    private class CreateProfileAdminPromoteHandler implements EventHandler<ActionEvent>{
        private HBox record;
        public CreateProfileAdminPromoteHandler(HBox i){record = i;}

        public void handle(ActionEvent e){
            String username = ((Label) record.getChildren().get(0)).getText();
            Account account = server.getAccount(username);

            if (account.getAccountType().equals("User")){
                server.updateProfileType(account, "Mod");
                record.getChildren().get(3).setVisible(true);
                record.getChildren().get(3).managedProperty().unbind();

            } else if (account.getAccountType().equals("Mod")){
                server.updateProfileType(account, "Admin");
                record.getChildren().get(4).setVisible(false);
                record.getChildren().get(4).managedProperty().bind(record.getChildren().get(4).visibleProperty());

            }
        }
    }

    private class CreateProfileAdminDemoteHandler implements EventHandler<ActionEvent>{
        private HBox record;
        public CreateProfileAdminDemoteHandler(HBox i){record = i;}

        public void handle(ActionEvent e){
            String username = ((Label) record.getChildren().get(0)).getText();
            Account account = server.getAccount(username);

            server.updateProfileType(account, "User");

            record.getChildren().get(3).setVisible(true);
            record.getChildren().get(3).managedProperty().unbind();

            record.getChildren().get(4).setVisible(false);
            record.getChildren().get(4).managedProperty().bind(record.getChildren().get(4).visibleProperty());

        }
    }

    private class CreateProfileAdminBanHandler implements EventHandler<ActionEvent>{
        private HBox record;
        public CreateProfileAdminBanHandler(HBox i){record = i;}

        public void handle(ActionEvent e){
            String username = ((Label) record.getChildren().get(0)).getText();
            Account account = server.getAccount(username);

            server.updateProfileBan(account, true);

            record.getChildren().get(5).setVisible(false);
            record.getChildren().get(5).managedProperty().bind(record.getChildren().get(5).visibleProperty());

            record.getChildren().get(6).setVisible(true);
            record.getChildren().get(6).managedProperty().unbind();

        }
    }

    private class CreateProfileAdminUnbanHandler implements EventHandler<ActionEvent>{
        private HBox record;
        public CreateProfileAdminUnbanHandler(HBox i){record = i;}

        public void handle(ActionEvent e){
            String username = ((Label) record.getChildren().get(0)).getText();
            Account account = server.getAccount(username);

            server.updateProfileBan(account, false);

            record.getChildren().get(6).setVisible(false);
            record.getChildren().get(6).managedProperty().bind(record.getChildren().get(5).visibleProperty());

            record.getChildren().get(5).setVisible(true);
            record.getChildren().get(5).managedProperty().unbind();

        }
    }

    private class CreateProfileAdminFindHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            for(HBox i: records){
                i.setVisible(true);
                i.managedProperty().bind(i.visibleProperty());

            }

            String text = findUsername.getText();

            for(HBox i: records){
                String username = ((Label) i.getChildren().get(0)).getText();

                if(!username.equalsIgnoreCase(text) && !(username.toUpperCase().startsWith(text.toUpperCase()))){
                    i.setVisible(false);
                    i.managedProperty().bind(i.visibleProperty());

                }
            }
        }
    }

    private class CreateProfileAdminResetHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            for(HBox i: records){
                i.setVisible(true);
                i.managedProperty().unbind();

            }
        }
    }
}

