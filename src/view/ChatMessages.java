package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import model.Account;
import model.ChatMessage;
import model.ServerConnection;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ChatMessages extends ScrollPane {
    private ArrayList<ChatMessage> messageList;
    private ServerConnection server;
    private Button btnDelete, btnTimeout;

    public ChatMessages(ServerConnection server){
        this.server = server;
        this.setStyle("-fx-background-color: transparent");
        this.setVvalue(1D);
        //this.maxHeight(1000);

    }

    public void refresh(){
        //LIMIT HOW MANY MESSAGES CAN BE SEEN
        //this.getChildren().clear();
        messageList = server.getChatMessages();
        VBox vbox = new VBox();

        while (messageList == null){
            messageList = server.getChatMessages();
        }

        //Format messages
        for(ChatMessage item : messageList){
            if(item.getRemoved().equals(true)){
                Label format = new Label("(Message removed)");
                vbox.getChildren().add(format);

            } else {
                Account account = server.getAccount(item.getUserId());

                while(account == null){
                    account = server.getAccount(item.getUserId());
                }
                Image image = new Image(new ByteArrayInputStream(account.getProfilePicture()));

                ImageView icon = new ImageView();
                icon.setImage(image);
                icon.setFitWidth(20); //20
                icon.setPreserveRatio(true);
                icon.setSmooth(true);
                icon.setCache(true);

                Circle clip = new Circle();
                clip.setRadius(10);
                clip.setCenterX(10);
                clip.setCenterY(10);

                icon.setClip(clip);
                Label format = new Label (item.getUserId() + ": " + item.getMessage());

                HBox hbox = new HBox();
                hbox.getChildren().addAll(icon,format);
                hbox.setSpacing(5);
                hbox.setPadding(new Insets(2,2,2,2));
                vbox.getChildren().add(hbox);
            }
        }
        this.setContent(vbox);
    }

    public void refreshMod(){
        //this.getChildren().clear();
        VBox vbox = new VBox();
        messageList = server.getChatMessages();

        for(ChatMessage item : messageList){
            if(item.getRemoved().equals(true)){
                Label format = new Label("(Message removed) " + item.getUserId() + ": " + item.getMessage());
                vbox.getChildren().add(format);

            } else {
                Account account = server.getAccount(item.getUserId());
                Image image = new Image(new ByteArrayInputStream(account.getProfilePicture()));

                ImageView icon = new ImageView();
                icon.setImage(image);
                icon.setFitWidth(20); //20
                icon.setPreserveRatio(true);
                icon.setSmooth(true);
                icon.setCache(true);

                Circle clip = new Circle();
                clip.setRadius(10);
                clip.setCenterX(10);
                clip.setCenterY(10);

                icon.setClip(clip);

                HBox hbox = new HBox();
                Label format = new Label (item.getUserId() + ": " + item.getMessage());

                btnDelete = new Button("Delete");
                btnTimeout = new Button("Timeout");

                btnDelete.setStyle("-fx-font-size: 10");
                btnTimeout.setStyle("-fx-font-size: 10");

                btnDelete.setOnAction(new CreateChatModDeleteHandler(hbox, item));
                btnTimeout.setOnAction(new CreateChatModTimeoutHandler(hbox, item));

                hbox.getChildren().addAll(icon,format,btnDelete,btnTimeout);
                hbox.setSpacing(5);
                hbox.setPadding(new Insets(2,2,2,2));
                vbox.getChildren().addAll(hbox);
            }
        }
        this.setContent(vbox);
    }

    private class CreateChatModDeleteHandler implements EventHandler<ActionEvent> {
        private HBox record;
        private ChatMessage message;

        public CreateChatModDeleteHandler(HBox i, ChatMessage j){
            record = i;
            message = j;
        }

        public void handle(ActionEvent e) {
            server.deleteChatMessage(message, true);
            server.broadcastRefreshChat();

        }
    }

    private class CreateChatModTimeoutHandler implements EventHandler<ActionEvent> {
        private HBox record;
        private ChatMessage message;

        public CreateChatModTimeoutHandler(HBox i, ChatMessage j){
            record = i;
            message = j;
        }
        public void handle(ActionEvent e) {
            server.deleteChatMessage(message, true);
            server.timeoutAccount(server.getAccount(message.getUserId()));
            server.broadcastRefreshChat();

        }
    }
}
