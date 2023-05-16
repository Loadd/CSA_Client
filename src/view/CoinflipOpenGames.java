package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import model.CoinflipGame;
import model.ServerConnection;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CoinflipOpenGames extends FlowPane {
    private ArrayList<CoinflipGameInfo> gamesView;
    private ServerConnection server;

    public CoinflipOpenGames(ServerConnection server){
        this.server = server;
        this.setVgap(8);
        this.setHgap(4);
        this.setAlignment(Pos.CENTER);

        gamesView = new ArrayList<>();

    }

    public void setWinner(CoinflipGame game, CreateBorder border) throws InterruptedException {
        ArrayList<CoinflipGameInfo> toRemove = new ArrayList<>();

        for (CoinflipGameInfo views: gamesView){
            if (views.getGame().getId().equals(game.getId())){
                TimeUnit.SECONDS.sleep(3);

                CoinflipGameInfo view = (CoinflipGameInfo) this.getChildren().get(gamesView.indexOf(views));
                border.updateMoney();

                if (game.getWinner().equals(game.getPlayerOneId())){
                    view.getUserOneBet().setText("+" + game.getBet());
                    view.getUserTwoBet().setText("-" + game.getBet());


                } else {
                    view.getUserOneBet().setText("-" + game.getBet());
                    view.getUserTwoBet().setText("+" + game.getBet());


                }
            }
        }


        //https://stackoverflow.com/a/2258080
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(border.getAccount().equals("Admin")){
                                    refreshAdmin();

                                } else{
                                    refresh();

                                }
                            }
                        });
                    }
                },
                5000
        );
        System.out.println("9");
    }

    private void addCoinflipGame(CoinflipGame game){
        //pass in client account to recognise own games
        CoinflipGameInfo display = new CoinflipGameInfo(game, server);
        gamesView.add(display);
        this.getChildren().add(display);

    }

    private void addCoinflipGameAdmin(CoinflipGame game){
        //pass in client account to recognise own games
        CoinflipGameInfo display = new CoinflipGameInfo(game, server);
        display.addAdmin();
        gamesView.add(display);
        this.getChildren().add(display);

    }

    public void refresh(){
        this.getChildren().clear();
        gamesView.clear();

        ArrayList<CoinflipGame> games = server.getOpenCoinflipGames();
        while(games == null){
            games = server.getOpenCoinflipGames();
        }

        if (!games.isEmpty()){
            for(CoinflipGame item : games){
                addCoinflipGame(item);

            }
        }
    }

    public void refreshAdmin(){
        this.getChildren().clear();
        gamesView.clear();

        ArrayList<CoinflipGame> games = server.getOpenCoinflipGames();
        if (!games.isEmpty()){
            for(CoinflipGame item : games){
                addCoinflipGameAdmin(item);

            }
        }
    }

    public void removeCoinflipGame(CoinflipGameInfo display, CoinflipOpenGames view){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getChildren().remove(display);
                gamesView.remove(display);
            }
        });


    }
}
