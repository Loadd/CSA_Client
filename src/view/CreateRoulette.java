package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.ServerConnection;

public class CreateRoulette extends VBox {

    private RouletteRollInfo rollInfo;
    private RouletteBetInfo betInfo;
    private Button howToPlay;

    public CreateRoulette(CreateBorder view, ServerConnection server) {
        betInfo = new RouletteBetInfo(server);
        rollInfo = new RouletteRollInfo(view, betInfo, server);

        howToPlay = new Button("How to Play");

        ObservableList vbList = this.getChildren();
        vbList.addAll(rollInfo, betInfo, howToPlay);

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(10);
    }

    public RouletteRollInfo getRollInfo(){
        return rollInfo;
    }

    public RouletteBetInfo getBetInfo(){
        return betInfo;
    }

    public void addHowToPlayHandler(EventHandler<ActionEvent> handler){
        howToPlay.setOnAction(handler);
    }

}
