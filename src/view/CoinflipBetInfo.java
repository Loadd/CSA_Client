package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CoinflipBetInfo extends HBox {
    private Label moneyIcon;
    private Button clear, pointZeroOne, pointOne, one, ten, oneHundred, half, timesTwo, max, createGame;
    private TextField moneyToBet;

    public CoinflipBetInfo(){
        this.setAlignment(Pos.CENTER);

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

        createGame = new Button("Create Game");

        ObservableList betCreationList = this.getChildren();
        betCreationList.addAll(moneyIcon, moneyToBet, clear, pointZeroOne, pointOne, one, ten, oneHundred, half, timesTwo, max, createGame);

        this.setSpacing(10);
        this.setMaxWidth(750);
        this.setMinHeight(50);
        this.setStyle("-fx-border-color: white; -fx-border-radius: 10;");
        this.setAlignment(Pos.CENTER);

    }

    public void addCreateGameEventHandler(EventHandler<ActionEvent> handler){createGame.setOnAction(handler);}

    public Float getMoneyToBet(){return Float.valueOf(moneyToBet.getText());}

    public void setMoneyToBet(Float e){moneyToBet.setText(e.toString());}

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

    public void addMaxHandler(EventHandler<ActionEvent> handler){max.setOnAction(handler);}
}
