package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Account;


//https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm

public class ProfileChangeInfo extends VBox {
    private Label lblProfilePicture, lblChangePassword, lblOldPassword, lblNewPassword, lblConfNewPassword;
    private TextField textProfilePicturePath;
    private PasswordField textOldPassword, textNewPassword, textNewPassword2;
    private Button btnBrowse, btnUpdate;
    private Account account;
    private byte[] image;

    public ProfileChangeInfo(Account i){
        account = i;
        HBox hbox = new HBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();

        //Profile pic
        lblProfilePicture = new Label("Profile Picture:");
        textProfilePicturePath = new TextField("");
        btnBrowse = new Button("Browse");

        hbox.setSpacing(5);
        hbox.getChildren().addAll(textProfilePicturePath, btnBrowse);

        //Change password
        lblChangePassword = new Label("Change Password:");

        lblOldPassword = new Label("Old Password: ");
        textOldPassword = new PasswordField();

        hbox1.setSpacing(5);
        hbox1.getChildren().addAll(lblOldPassword, textOldPassword);

        lblNewPassword = new Label("New Password: ");
        textNewPassword = new PasswordField();

        hbox2.setSpacing(5);
        hbox2.getChildren().addAll(lblNewPassword, textNewPassword);

        lblConfNewPassword = new Label("Confirm New \nPassword: ");
        textNewPassword2 = new PasswordField();

        hbox3.setSpacing(5);
        hbox3.getChildren().addAll(lblConfNewPassword, textNewPassword2);

        //update
        btnUpdate = new Button("Update");

        this.setSpacing(5);
        this.getChildren().addAll(lblProfilePicture, hbox, lblChangePassword,
                hbox1, hbox2, hbox3, btnUpdate);

    }

    public String getOldPassword(){
        return textOldPassword.getText();
    }

    public String getNewPassword(){
        return textNewPassword.getText();
    }

    public String getNewPassword2(){
        return textNewPassword2.getText();
    }

    public byte[] getImage() { return image;}

    public void setProfilePicturePath(String e){ textProfilePicturePath.setText(e);}

    public void setImage(byte[] e){ image = e;}

    public void addBrowseEventHandler(EventHandler<ActionEvent> handler){btnBrowse.setOnAction(handler);}
    public void addUpdateEventHandler(EventHandler<ActionEvent> handler){btnUpdate.setOnAction(handler);}
}
