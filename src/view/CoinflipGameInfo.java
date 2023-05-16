package view;

import controller.CSAController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import model.Account;
import model.CoinflipGame;
import model.ServerConnection;

import java.io.ByteArrayInputStream;

public class CoinflipGameInfo extends GridPane {
    private Label moneyIcon, userOneIcon, userOneId, userOneBet, userTwoId, userTwoBet, vs, nullIcon;
    private Button join, forceP1Win, forceP2Win;
    private CoinflipGame game;
    private ServerConnection server;

    public CoinflipGameInfo(CoinflipGame a, ServerConnection b){
        this.game = a;
        this.server = b;

        vs = new Label("VS");
        nullIcon = new Label();
        join = new Button("Join");

        join.setOnAction(new CSAController.CreateCoinflipJoinGameHandler(game));

        Account p1 = server.getAccount(a.getPlayerOneId());

        //Player One information
        Image image = new Image(new ByteArrayInputStream(p1.getProfilePicture()));

        ImageView userOneIcon = new ImageView();
        userOneIcon.setImage(image);
        userOneIcon.setFitWidth(20); //20
        userOneIcon.setPreserveRatio(true);
        userOneIcon.setSmooth(true);
        userOneIcon.setCache(true);

        Circle clip = new Circle();
        clip.setRadius(10);
        clip.setCenterX(10);
        clip.setCenterY(10);

        userOneIcon.setClip(clip);

        userOneId = new Label(a.getPlayerOneId());
        userOneBet = new Label(a.getBet().toString());

        /* | |P1 Logo|  |        | | | |
           | | P1 Id |  |        | | | |
           | | P1 $  |VS|P2 Logo | | | |
           | |       |  | P2 Id  | | | |
           | |       |  | P2 $   | | | |
           | |       |  |        | | | |
         */

        //Player Two Information
        if (a.getPlayerTwoId() != null){
            System.out.println("1");
            image = new Image(new ByteArrayInputStream(server.getAccount(a.getPlayerTwoId()).getProfilePicture()));

            ImageView userTwoIcon = new ImageView();
            userTwoIcon.setImage(image);
            userTwoIcon.setFitWidth(20); //20
            userTwoIcon.setPreserveRatio(true);
            userTwoIcon.setSmooth(true);
            userTwoIcon.setCache(true);

            userTwoId = new Label(a.getPlayerTwoId());
            userTwoBet = new Label(a.getBet().toString());
            this.add(userTwoIcon, 3,2);
            this.add(userTwoId, 3,3);


        } else {
            System.out.println("2");
            Label userTwoIconLAB = new Label("?");
            userTwoId = new Label("Player Two Id");
            userTwoBet = new Label(a.getBet().toString());
            this.add(nullIcon,3, 2);
            this.add(join,3, 3);
            userTwoIconLAB.setTextAlignment(TextAlignment.RIGHT);

        }


        userOneBet.setTextAlignment(TextAlignment.RIGHT);
        userTwoBet.setTextAlignment(TextAlignment.RIGHT);
        userOneId.setAlignment(Pos.CENTER_RIGHT);
        userTwoId.setTextAlignment(TextAlignment.RIGHT);

        join.setAlignment(Pos.CENTER);

        this.add(userOneIcon, 1, 0);
        this.add(userOneId, 1, 1);
        this.add(userOneBet, 1,2);
        this.add(vs, 2,2);
        this.add(userTwoBet,3, 4);


        this.setStyle("-fx-border-color: white;");

    }

    public void addAdmin(){
        forceP1Win = new Button("Force win");
        forceP2Win = new Button("Force win");

        forceP1Win.setOnAction(new CreateCoinflipAdminOverrideP1Handler());
        forceP2Win.setOnAction(new CreateCoinflipAdminOverrideP2Handler());

        forceP2Win.setVisible(false);
        forceP2Win.managedProperty().bind(forceP2Win.visibleProperty());

        this.add(forceP1Win, 1,3);
        this.add(forceP2Win,3,5);
    }

    public void setPlayerTwo(String playerTwoId){
        //getIcon
        userTwoId.setText(playerTwoId);
        //replaces join button
        this.add(userTwoId, 3,3);

        forceP2Win.setVisible(true);
        forceP2Win.managedProperty().unbind();
    }

    public CoinflipGame getGame(){return game;}

    public Button getJoin(){return join;}

    public Label getUserTwoBet(){return userTwoBet;}

    public Label getUserOneBet(){return userOneBet;}

    public void setGame(CoinflipGame game){this.game = game;}

    private class CreateCoinflipAdminOverrideP1Handler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            server.forceCoinflipWinner(game, server.getAccount(game.getPlayerOneId()));

        }
    }

    private class CreateCoinflipAdminOverrideP2Handler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            server.forceCoinflipWinner(game, server.getAccount(game.getPlayerTwoId()));
        }
    }
}
