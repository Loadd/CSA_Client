package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Account;
import model.Game;
import model.ServerConnection;


public class CreateBorder extends BorderPane {

    private ComboBox<Game> cboGames;
    private Button btnLogin, btnProfile, btnBack;
    private Label lblMoney, lblLogo, lblTitle;
    private CreateRoulette roulette;
    private CreateCoinflip coinflip;
    private CreateAccountDetails accountDetails;
    private CreateChat chat;
    private CreateProfile profile;
    private Account account;
    private ServerConnection server;

    public CreateBorder(ServerConnection server) {
        this.server = server;
        account = new Account();
        roulette = new CreateRoulette(this, server);
        coinflip = new CreateCoinflip(server);
        chat = new CreateChat(server);
        accountDetails = new CreateAccountDetails();

        HBox hbox = new HBox(15);

        lblLogo = new Label("Logo Here");
        lblLogo.setMinWidth(50);
        lblLogo.setMaxWidth(100);
        lblLogo.setPrefHeight(40);
        hbox.setHgrow(lblLogo, Priority.ALWAYS);

        cboGames = new ComboBox<>();
        cboGames.setMaxWidth(200);
        cboGames.setMinWidth(100);
        cboGames.setPrefHeight(50);

        hbox.setHgrow(cboGames, Priority.ALWAYS);

        lblTitle = new Label("Guest");
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        lblTitle.setAlignment(Pos.CENTER);
        lblTitle.setStyle("-fx-font-size: 20");
        hbox.setHgrow(lblTitle, Priority.ALWAYS);

        lblMoney = new Label("");
        lblMoney.setMaxWidth(100);
        lblMoney.setMinWidth(50);
        lblMoney.setPrefHeight(40);
        hbox.setHgrow(lblMoney, Priority.ALWAYS);

        btnLogin = new Button("Login");
        btnLogin.setMaxWidth(100);
        btnLogin.setMinWidth(50);
        btnLogin.setPrefHeight(40);
        hbox.setHgrow(btnLogin, Priority.ALWAYS);

        btnProfile = new Button();
        btnProfile.setMaxWidth(100);
        btnProfile.setMinWidth(50);
        btnProfile.setPrefHeight(40);
        hbox.setHgrow(btnProfile, Priority.ALWAYS);
        //https://stackoverflow.com/a/12201442
        btnProfile.managedProperty().bind(btnProfile.visibleProperty());
        btnProfile.setVisible(false);

        btnBack = new Button("Back");
        btnBack.setMaxWidth(100);
        btnBack.setMinWidth(50);
        btnBack.setPrefHeight(40);
        hbox.setHgrow(btnBack, Priority.ALWAYS);
        btnBack.managedProperty().bind(btnBack.visibleProperty());
        btnBack.setVisible(false);

        ObservableList hbList = hbox.getChildren();
        hbList.addAll(lblLogo, cboGames, lblTitle, lblMoney, btnLogin, btnProfile, btnBack);
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setAlignment(Pos.CENTER);

        this.setTop(hbox);
        this.setLeft(chat);

        //Design
        this.setStyle("-fx-background-color: rgb(18,18,18)");
        this.getStylesheets().add("/assets/Border.css");

    }

    public CreateRoulette getRoulette(){return roulette;}

    public CreateCoinflip getCoinflip(){return coinflip;}

    public CreateChat getChat(){return chat;}

    public CreateProfile getProfile(){return profile;}

    public CreateAccountDetails getAccountDetails(){return accountDetails;}

    public Account getAccount(){return account;}

    public Button getBtnLogin(){return btnLogin;}

    public Game getComboChoice(){return cboGames.getSelectionModel().getSelectedItem();}

    public void addGamesToComboBox(Game[] games) {
        cboGames.getItems().addAll(games);
        cboGames.getSelectionModel().select(0);
    }

    public void setProfile(Account a){
        profile = new CreateProfile(a, server);
    }

    public void setLoggedOutBorder(){
        btnLogin.setVisible(true);

        btnBack.managedProperty().bind(btnBack.visibleProperty());
        btnBack.setVisible(false);
    }

    public void setLoginBorder(){
        btnLogin.managedProperty().bind(btnLogin.visibleProperty());
        btnLogin.setVisible(false);

        btnBack.managedProperty().bind(btnBack.visibleProperty());
        btnBack.setVisible(false);

        btnProfile.setVisible(true);
    }

    public void setLoginBorder(Account a){
        //CHECK PERMISSION STATUS
        account = a;
        btnLogin.managedProperty().bind(btnLogin.visibleProperty());
        btnLogin.setVisible(false);

        btnBack.managedProperty().bind(btnBack.visibleProperty());
        btnBack.setVisible(false);

        btnProfile.setVisible(true);
        btnProfile.setText(a.getUsername());

        lblTitle.setText(a.getAccountType());
        lblMoney.setText(a.getMoney().toString());
    }


    public void setRegisterBorder(){
        btnBack.setVisible(true);

        btnLogin.managedProperty().bind(btnLogin.visibleProperty());
        btnLogin.setVisible(false);
    }

    public void setProfileBorder(){
        btnBack.setVisible(true);

        btnProfile.managedProperty().bind(btnProfile.visibleProperty());
        btnProfile.setVisible(false);
    }

    public void updateMoney(){
        Account latest = server.getAccount(account.getUsername());
        lblMoney.setText("$" + latest.getMoney().toString());
    }

    public void refreshIcons(byte[] i){
        //border
        //profile view
        profile.getProfileDisplay().setPicture(i);
    }

    public void updateMoney(Float a){
        lblMoney.setText("$" + a.toString());
    }

    public void addComboHandler(EventHandler<ActionEvent> handler){
        cboGames.setOnAction(handler);
    }

    public void addLoginHandler(EventHandler<ActionEvent> handler){
        btnLogin.setOnAction(handler);
    }

    public void addBackHandler(EventHandler<ActionEvent> handler){
        btnBack.setOnAction(handler);
    }

    public void addProfileHandler(EventHandler<ActionEvent> handler){ btnProfile.setOnAction(handler);}

}
