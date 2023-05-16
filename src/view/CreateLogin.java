package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateLogin extends VBox {
    private Label lblUsername, lblPassword;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button login;

    public CreateLogin() {
        HBox row1 = new HBox();
        HBox row2 = new HBox();

        lblUsername = new Label("Username:");
        lblPassword = new Label("Password:");

        lblUsername.setStyle("-fx-font-size: 20");
        lblPassword.setStyle("-fx-font-size: 20");

        txtUsername = new TextField();
        txtPassword = new PasswordField();

        login = new Button("Login");

        ObservableList row1List = row1.getChildren();
        row1List.addAll(lblUsername, txtUsername);
        row1.setAlignment(Pos.CENTER);
        row1.setSpacing(5);

        ObservableList row2List = row2.getChildren();
        row2List.addAll(lblPassword, txtPassword);
        row2.setAlignment(Pos.CENTER);
        row2.setSpacing(5);

        ObservableList vbList = this.getChildren();
        vbList.addAll(row1, row2, login);

        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);

    }
    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return txtPassword.getText();
    }

    public void addLoginHandler(EventHandler<ActionEvent> handler){ login.setOnAction(handler); }

}
