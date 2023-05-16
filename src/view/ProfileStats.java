package view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.util.ArrayList;

public class ProfileStats extends VBox {
    private Account account;
    private ServerConnection server;

    public ProfileStats(Account i, ServerConnection j){
        account = i;
        server = j;

        HBox totalBets = new HBox();
        Label lblTotalBets = new Label("Total Bets: ");
        Label tbTotal = new Label(getTotalBets());

        lblTotalBets.setStyle("-fx-font-size: 20");
        tbTotal.setStyle("-fx-font-size: 20");

        totalBets.getChildren().addAll(lblTotalBets, tbTotal);

        HBox totalRouletteBets = new HBox();
        Label lblTotalRouletteBets = new Label("Total Roulette Bets: ");
        Label rbTotal = new Label(getTotalRouletteBets());

        lblTotalRouletteBets.setStyle("-fx-font-size: 20");
        rbTotal.setStyle("-fx-font-size: 20");

        totalRouletteBets.getChildren().addAll(lblTotalRouletteBets, rbTotal);

        HBox totalCoinflipBets = new HBox();
        Label lblTotalCoinflipBets = new Label("Total Coinflip Bets: ");
        Label cbTotal = new Label(getTotalCoinflipBets());

        totalCoinflipBets.setStyle("-fx-font-size: 20");
        lblTotalCoinflipBets.setStyle("-fx-font-size: 20");

        totalCoinflipBets.getChildren().addAll(lblTotalCoinflipBets, cbTotal);

        HBox totalWinnings = new HBox();
        Label lblTotalWinnings = new Label("Total Winnings: ");
        Label wTotal = new Label("$" + getTotalWinnings());

        lblTotalWinnings.setStyle("-fx-font-size: 20");
        wTotal.setStyle("-fx-font-size: 20");

        totalWinnings.getChildren().addAll(lblTotalWinnings, wTotal);

        HBox totalLosses = new HBox();
        Label lblTotalLosses = new Label("Total Losses: ");
        Label lTotal = new Label("$" + getTotalLosses());

        lblTotalLosses.setStyle("-fx-font-size: 20");
        lTotal.setStyle("-fx-font-size: 20");

        totalLosses.getChildren().addAll(lblTotalLosses, lTotal);

        this.setStyle("-fx-background-color: rgb(30,30,30)");
        this.getChildren().addAll(totalBets,totalRouletteBets, totalCoinflipBets, totalWinnings, totalLosses);

    }

    private String getTotalBets(){
        ArrayList<CoinflipGame> cfGames = server.getCoinflipGames(account.getUsername());
        ArrayList<RouletteBet> rGames = server.getRouletteBets(account.getUsername());

        Integer count = cfGames.size() + rGames.size();

        return count.toString();

    }

    private String getTotalRouletteBets(){
        ArrayList<RouletteBet> rGames = server.getRouletteBets(account.getUsername());
        Integer count = rGames.size();

        return count.toString();

    }

    private String getTotalCoinflipBets(){
        ArrayList<CoinflipGame> cfGames = server.getCoinflipGames(account.getUsername());
        Integer count = cfGames.size();

        return count.toString();

    }

    private String getTotalWinnings(){
        ArrayList<CoinflipGame> cfGames = server.getCoinflipGames(account.getUsername());
        Float count = (float) 0;

        for(CoinflipGame item: cfGames){
            if(item.getWinner().equals(account.getUsername())){
                count += item.getBet();
            }
        }

        ArrayList<RouletteBet> rGames = server.getRouletteBets(account.getUsername());
        for(RouletteBet item: rGames){
            RouletteRoll roll = server.getRouletteRollFromId(item.getRollId());

            if(roll.getRollResult().equals(item.getColour())){
                count += item.getMoneyBet();
            }
        }

        return count.toString();

    }

    private String getTotalLosses(){
        ArrayList<CoinflipGame> cfGames = server.getCoinflipGames(account.getUsername());
        Float count = (float) 0;

        for(CoinflipGame item: cfGames){
            if(!item.getWinner().equals(account.getUsername())){
                count += item.getBet();
            }
        }

        ArrayList<RouletteBet> rGames = server.getRouletteBets(account.getUsername());
        for(RouletteBet item: rGames){
            RouletteRoll roll = server.getRouletteRollFromId(item.getRollId());

            if(!roll.getRollResult().equals(item.getColour())){
                count += item.getMoneyBet();
            }
        }

        return count.toString();

    }
}
