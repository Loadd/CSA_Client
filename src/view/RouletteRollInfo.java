package view;

import com.sun.scenario.animation.AnimationPulseMBean;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class RouletteRollInfo extends VBox {
    private Label lblRollTimer, lblRollAnimation, lblPrevRolls;
    private Button btnRed, btnGreen, btnBlack;
    private String prevFormat;
    private ImageView coins;
    private RouletteRoll currentRoll;
    private RouletteBetInfo betInfo;
    private CreateBorder view;
    private ServerConnection server;

    public RouletteRollInfo(CreateBorder view, RouletteBetInfo betInfo, ServerConnection server) {
        this.view = view;
        this.betInfo = betInfo;
        this.server = server;

        prevFormat = "";
        ArrayList<RouletteRoll> rolls = server.getRouletteRollPrevious();

        for(RouletteRoll roll: rolls){
            prevFormat += roll.getRollResult() + "~";

        }

        coins = new ImageView();
        coins.setImage(new Image("/assets/roulettecoins.png"));
        coins.setFitWidth(600); //20
        coins.setPreserveRatio(true);
        coins.setSmooth(true);
        coins.setCache(true);

        coins.setViewport(new Rectangle2D(0,0, 2000, 200));

        lblRollTimer = new Label("Roll Timer");
        lblRollAnimation = new Label("Roll Animation");
        lblPrevRolls = new Label(prevFormat);

        lblRollTimer.setStyle("-fx-font-size: 30");
        lblPrevRolls.setStyle("-fx-font-size: 15");

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(lblRollTimer, coins, lblPrevRolls);
        this.setSpacing(20);

    }

    public void addAdmin(){
        HBox hbox = new HBox();
        Label text = new Label("Override Next Roll");
        btnRed = new Button("Red");
        btnGreen = new Button("Green");
        btnBlack = new Button("Black");
        btnRed.setOnAction(new CreateAdminOverrideRollHandler("red"));
        btnGreen.setOnAction(new CreateAdminOverrideRollHandler("green"));
        btnBlack.setOnAction(new CreateAdminOverrideRollHandler("black"));

        hbox.getChildren().addAll(text,btnRed,btnGreen,btnBlack);
        hbox.setSpacing(3);
        hbox.setAlignment(Pos.CENTER);
        this.getChildren().add(hbox);

    }

    public Animation redAnimation(ObjectProperty<Integer> coinsProperty){
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double v) {
                int coinsOffset = (int) ((v * 58000) % 3950);
                coinsProperty.set(coinsOffset);
            }
        };
        animation.setCycleCount((1));
        return animation;

    }

    public Animation blackAnimation(ObjectProperty<Integer> coinsProperty){
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double v) {
                int coinsOffset = (int) ((v * 58000) % 3850);
                coinsProperty.set(coinsOffset);
            }
        };
        animation.setCycleCount((1));
        return animation;

    }

    public Animation greenAnimation(ObjectProperty<Integer> coinsProperty){
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double v) {
                int coinsOffset = (int) ((v * 58000) % 2950);
                coinsProperty.set(coinsOffset);
            }
        };
        animation.setCycleCount((1));
        return animation;

    }

    public void updatePrevRolls(){
        ArrayList<RouletteRoll> rolls = server.getRouletteRollPrevious();
        prevFormat = "";

        for(RouletteRoll roll: rolls){
            prevFormat += roll.getRollResult() + "~";
        }

        lblPrevRolls.setText(prevFormat);

    }

    public void updateTimer(Integer a){lblRollTimer.setText(a.toString());}

    public void updateColour(String a){
        //https://www.pragmaticcoding.ca/javafx/elements/sprites
        coins.setViewport(new Rectangle2D(0,0, 2000, 200));

        ObjectProperty<Integer> coinsOffsetProperty = new SimpleObjectProperty<>(0);
        coinsOffsetProperty.addListener(observable -> {
            coins.setViewport(new Rectangle2D(coinsOffsetProperty.get(), 0, 2000, 200));
        });

        if (a.equals("red")){
            redAnimation(coinsOffsetProperty).play();
        } else if (a.equals("black")){
            blackAnimation(coinsOffsetProperty).play();
        } else {
            greenAnimation(coinsOffsetProperty).play();
        }
    }

    public String getTimer(){return lblRollTimer.getText();}

    private class CreateAdminOverrideRollHandler implements EventHandler<ActionEvent> {
        private String colour;
        public CreateAdminOverrideRollHandler(String i){colour = i;}

        public void handle(ActionEvent e) {
            RouletteRoll roll = server.getRouletteRoll();
            server.overrideRouletteColour(roll, colour);

        }
    }
}


