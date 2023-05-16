package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.ServerConnection;

public class CreateCoinflip extends VBox {

    private Label title;
    private CoinflipBetInfo coinflipBettingInfo;
    private CoinflipOpenGames coinflipOpenGames;
    private Button howToPlay;

    public CreateCoinflip(ServerConnection server) {
        title = new Label("Coinflip");
        title.setStyle("-fx-font-size: 20");

        howToPlay = new Button("How to Play");

        //create Betting information
        coinflipBettingInfo = new CoinflipBetInfo();

        //create open games flow pane
        coinflipOpenGames = new CoinflipOpenGames(server);

        ObservableList vbList = this.getChildren();
        vbList.addAll(title, coinflipBettingInfo, coinflipOpenGames, howToPlay);

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(15);

    }

    public CoinflipBetInfo getBettingInfo() {return coinflipBettingInfo;}

    public CoinflipOpenGames getOpenGames() {return coinflipOpenGames;}

    public void addHowToPlayHandler(EventHandler<ActionEvent> handler){
        howToPlay.setOnAction(handler);
    }
}
