package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import model.RouletteBet;
import model.ServerConnection;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class RouletteCurrentBetInfo extends VBox {
    private Button placeBet;
    private Label totalBets, totalMoney;
    private ObservableList vbList;
    private VBox vbox;
    private ArrayList<HBox> records;

    public RouletteCurrentBetInfo(String colour){

        records = new ArrayList<>();
        placeBet = new Button("Bet " + colour);

        //create table title
        totalBets = new Label("totalBets: 0");
        totalMoney = new Label(" | totalMoney: $0");

        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.getChildren().addAll(totalBets, totalMoney);

        vbox = new VBox();
        vbox.getChildren().add(title);

        vbox.setMinWidth(150);
        vbox.setMaxWidth(200);
        vbox.setMaxHeight(300);
        vbox.setStyle("-fx-border-color: white; -fx-border-radius: 15;");

        //add table to clas
        vbList = this.getChildren();
        this.setAlignment(Pos.CENTER);
        vbList.addAll(placeBet, vbox);

        this.setSpacing(5);
    }

    public void addPlaceBetEventHandler(EventHandler<ActionEvent> handler){ placeBet.setOnAction(handler); }

    public void addRecord(String username, String moneyBet, byte[] image){
        Image img = new Image(new ByteArrayInputStream(image));

        ImageView icon = new ImageView();
        icon.setImage(img);
        icon.setFitWidth(20); //20
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setCache(true);

        Circle clip = new Circle();
        clip.setRadius(10);
        clip.setCenterX(10);
        clip.setCenterY(10);

        icon.setClip(clip);

        //if (vbList.size() > 2){
        //    vbList.remove(2, vbList.size());
        //    HBox record = new HBox();
        //    record.setAlignment(Pos.CENTER);
//
        //    Label lblUsername = new Label(username);
        //    Label lblUserMoney = new Label(moneyBet);
        //    record.getChildren().addAll(icon, lblUsername, lblUserMoney);
        //    //records.add(record);
        //    //vbList.addAll(records);
        //    record.setSpacing(3);
        //    vbox.getChildren().add(record);
        //    records.add(record);
//
        //    //adds each of the records rather than one big record
        //    //IF RECORD EXISTS UPDATE (IF USER WANTS TO BET MORE ON SAME COLOUR)
//
        //} else{
        //    HBox record = new HBox();
        //    record.setAlignment(Pos.CENTER);
        //    Label lblUsername = new Label(username);
        //    Label lblUserMoney = new Label(moneyBet);
        //    record.getChildren().addAll(icon, lblUsername, lblUserMoney);
        //    record.setSpacing(3);
        //    //records.add(record);
        //    //vbList.addAll(records);
        //    vbox.getChildren().add(record);
        //    records.add(record);
        //}

        HBox record = new HBox();
        record.setAlignment(Pos.CENTER);
        Label lblUsername = new Label(username);
        Label lblUserMoney = new Label(moneyBet);
        record.getChildren().addAll(icon, lblUsername, lblUserMoney);
        record.setSpacing(3);
        vbox.getChildren().add(record);
        records.add(record);

    }

    public void clearRecords(){
        if(vbox.getChildren().size() > 1){
            totalBets.setText("totalBets: 0");
            totalMoney.setText(" | totalMoney: $0");

            vbox.getChildren().remove(1, vbox.getChildren().size());
            records.clear();

        }
    }

    public void setTotals(Integer i, Float j){
        totalBets.setText("totalBets: "+i);
        totalMoney.setText(" | totalMoney: $"+j);

    }
}
