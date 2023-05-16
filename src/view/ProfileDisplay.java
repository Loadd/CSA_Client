package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import model.Account;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//https://www.tutorialspoint.com/javafx/javafx_images.htm

public class ProfileDisplay extends VBox {
    private Image image;
    private Label username;
    private Account account;

    public ProfileDisplay(Account i) {
        this.account = i;
        image = new Image(new ByteArrayInputStream(account.getProfilePicture()));

        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html
        ImageView icon = new ImageView();
        icon.setImage(image);
        icon.setFitWidth(100);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setCache(true);

        Circle clip = new Circle();
        clip.setRadius(50);
        clip.setCenterX(50);
        clip.setCenterY(50);

        icon.setClip(clip);

        username = new Label(account.getUsername());

        this.setSpacing(10);
        this.setMinHeight(150);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-color: white");
        this.getChildren().addAll(icon, username);

    }

    public void setPicture(byte[] i){
        image = new Image(new ByteArrayInputStream(i));
        this.getChildren().clear();

        ImageView e = new ImageView();
        e.setImage(image);
        e.setFitWidth(100);
        e.setPreserveRatio(true);
        e.setSmooth(true);
        e.setCache(true);

        Circle clip = new Circle();
        clip.setRadius(50);
        clip.setCenterX(50);
        clip.setCenterY(50);

        e.setClip(clip);

        username = new Label(account.getUsername());

        this.getChildren().addAll(e, username);

    }
}
