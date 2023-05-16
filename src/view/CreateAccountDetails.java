package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CreateAccountDetails extends VBox {

    public CreateLogin loginPage;
    public CreateRegister registerPage;
    private Button toRegister, toLogin;
    private ObservableList vbList;

    public CreateAccountDetails() {
        loginPage = new CreateLogin();
        registerPage = new CreateRegister();

        toRegister = new Button("Register Here");
        toLogin = new Button("Login Here");

        vbList = this.getChildren();
        vbList.addAll(loginPage, toRegister);

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        //this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    public ObservableList getVbList() {return vbList;}

    public Button getToLogin() {return toLogin;}

    public Button getToRegister() {return toRegister;}

    public CreateRegister getRegisterPage() {return registerPage;}

    public CreateLogin getLoginPage() {return loginPage;}

    public void addToLoginHandler(EventHandler<ActionEvent> handler){
        toLogin.setOnAction(handler);
    }

    public void addToRegisterHandler(EventHandler<ActionEvent> handler){
        toRegister.setOnAction(handler);
    }
}
