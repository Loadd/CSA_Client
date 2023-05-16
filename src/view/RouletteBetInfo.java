package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.RouletteBet;
import model.ServerConnection;

import java.util.ArrayList;

public class RouletteBetInfo extends VBox {
    private Label moneyIcon;
    private Button clear, pointZeroOne, pointOne, one, ten, oneHundred, half, timesTwo, max;
    private TextField moneyToBet;
    private RouletteCurrentBetInfo betInfoRed, betInfoGreen, betInfoBlack;
    private ServerConnection server;

    public RouletteBetInfo(ServerConnection server){
        this.server = server;

        HBox betCreationInfo = new HBox();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        //betCreation
        moneyIcon = new Label("$");
        moneyToBet = new TextField("0");

        clear = new Button("clear");
        clear.setOnAction(new Clear());

        pointZeroOne = new Button ("+.01");
        pointZeroOne.setOnAction(new PointZeroOne());

        pointOne = new Button ("+.1");
        pointOne.setOnAction(new PointOne());

        one = new Button("+ 1");
        one.setOnAction(new One());

        ten = new Button("+ 10");
        ten.setOnAction(new Ten());

        oneHundred = new Button("+ 100");
        oneHundred.setOnAction(new OneHundred());

        half = new Button("/2");
        half.setOnAction(new Half());

        timesTwo = new Button("*2");
        timesTwo.setOnAction(new TimesTwo());

        max = new Button("Max");

        ObservableList betCreationList = betCreationInfo.getChildren();
        betCreationList.addAll(moneyIcon, moneyToBet, clear, pointZeroOne, pointOne, one, ten, oneHundred, half, timesTwo, max);

        betCreationInfo.setSpacing(10);
        betCreationInfo.setMaxWidth(650);
        betCreationInfo.setMinHeight(50);
        betCreationInfo.setStyle("-fx-border-color: white; -fx-border-radius: 10;");
        betCreationInfo.setAlignment(Pos.CENTER);

        betInfoRed = new RouletteCurrentBetInfo("red");
        betInfoGreen = new RouletteCurrentBetInfo("green");
        betInfoBlack = new RouletteCurrentBetInfo("black");

        ObservableList hbList = hbox.getChildren();
        hbList.addAll(betInfoRed, betInfoGreen, betInfoBlack);

        hbox.setSpacing(100);

        //last
        ObservableList vbList = this.getChildren();
        vbList.addAll(betCreationInfo, hbox);

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        //this.setStyle("-fx-border-color: white");
    }

    public RouletteCurrentBetInfo getCurrentBetInfoRed() {
        return betInfoRed;
    }

    public RouletteCurrentBetInfo getCurrentBetInfoGreen() {
        return betInfoGreen;
    }

    public RouletteCurrentBetInfo getCurrentBetInfoBlack() {
        return betInfoBlack;
    }

    public Float getRouletteBettingMoney() {
        //VALIDATION
        return Float.valueOf(moneyToBet.getText());
    }

    public void setRouletteBettingMoney(Float e) {
        moneyToBet.setText(e.toString());
    }

    private class Clear implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            moneyToBet.setText("0");
        }
    }

    private class PointZeroOne implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float increment = (float) 0.01;
            Float money = new Float(moneyToBet.getText());

            if(money.equals(0)){
                moneyToBet.setText(increment.toString());
            } else {
                money += increment;
                moneyToBet.setText(money.toString());
            }
        }
    }

    private class PointOne implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float increment = (float) 0.1;
            Float money = new Float(moneyToBet.getText());

            if(money.equals(0)){
                moneyToBet.setText(increment.toString());
            } else {
                money += increment;
                moneyToBet.setText(money.toString());
            }
        }
    }

    private class One implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float increment = (float) 1;
            Float money = new Float(moneyToBet.getText());

            if(money.equals(0)){
                moneyToBet.setText(increment.toString());
            } else {
                money += increment;
                moneyToBet.setText(money.toString());
            }
        }
    }

    private class Ten implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float increment = (float) 10;
            Float money = new Float(moneyToBet.getText());

            if(money.equals(0)){
                moneyToBet.setText(increment.toString());
            } else {
                money += increment;
                moneyToBet.setText(money.toString());
            }
        }
    }

    private class OneHundred implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float increment = (float) 100;
            Float money = new Float(moneyToBet.getText());

            if(money.equals(0)){
                moneyToBet.setText(increment.toString());
            } else {
                money += increment;
                moneyToBet.setText(money.toString());
            }
        }
    }

    private class Half implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float money = new Float(moneyToBet.getText());

            money = money / 2;
            moneyToBet.setText(money.toString());

        }
    }

    private class TimesTwo implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e){
            Float money = new Float(moneyToBet.getText());

            money = money * 2;
            moneyToBet.setText(money.toString());
        }
    }

    public void refreshBets(){
        betInfoRed.clearRecords();
        betInfoBlack.clearRecords();
        betInfoGreen.clearRecords();

        Integer count = 0;
        Float money = (float) 0;

        ArrayList<RouletteBet> redBets = server.getRouletteBets(server.getRouletteRoll().getRollId(), "red");
        ArrayList<RouletteBet> greenBets = server.getRouletteBets(server.getRouletteRoll().getRollId(), "green");
        ArrayList<RouletteBet> blackBets = server.getRouletteBets(server.getRouletteRoll().getRollId(), "black");

        for (RouletteBet item: redBets){
            betInfoRed.addRecord(item.getUserId(), item.getMoneyBet().toString(), server.getAccount(item.getUserId()).getProfilePicture());
            count += 1;
            money += item.getMoneyBet();

        }
        betInfoRed.setTotals(count, money);
        count = 0;
        money = (float) 0;

        for (RouletteBet item: greenBets){
            betInfoGreen.addRecord(item.getUserId(), item.getMoneyBet().toString(), server.getAccount(item.getUserId()).getProfilePicture());
            count += 1;
            money += item.getMoneyBet();

        }
        betInfoGreen.setTotals(count, money);
        count = 0;
        money = (float) 0;

        for (RouletteBet item: blackBets){
            betInfoBlack.addRecord(item.getUserId(), item.getMoneyBet().toString(), server.getAccount(item.getUserId()).getProfilePicture());
            count += 1;
            money += item.getMoneyBet();

        }
        betInfoBlack.setTotals(count, money);

    }

    public void addMaxHandler(EventHandler<ActionEvent> handler){max.setOnAction(handler);}
}
