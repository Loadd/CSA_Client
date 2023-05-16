package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import model.*;
import view.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class CSAController {
    private static CreateBorder view;
    private CreateRoulette roulette;
    private CreateCoinflip coinflip;
    private CreateAccountDetails accountDetails;
    private CreateChat chat;
    private CreateProfile profile;
    private MessageHandler messageHandler;
    private static ServerConnection server;

    public CSAController(CreateBorder view, ServerConnection server){
        //probs wont work
        this.server = server;
        this.view = view;
        view.addGamesToComboBox(this.getGamesFromserver());

        messageHandler = new MessageHandler();
        messageHandler.start();

        roulette = view.getRoulette();
        coinflip = view.getCoinflip();
        chat = view.getChat();
        accountDetails = view.getAccountDetails();

        coinflip.getOpenGames().refresh();
        chat.getChatMessages().refresh();

        view.setCenter(roulette);
        this.attachEventHandlers();

    }

    private void attachEventHandlers(){
        view.addComboHandler(new ComboBoxHandler());
        roulette.getBetInfo().getCurrentBetInfoBlack().addPlaceBetEventHandler(new CreateRouletteBetHandlerBlack());
        roulette.getBetInfo().getCurrentBetInfoGreen().addPlaceBetEventHandler(new CreateRouletteBetHandlerGreen());
        roulette.getBetInfo().getCurrentBetInfoRed().addPlaceBetEventHandler(new CreateRouletteBetHandlerRed());
        roulette.getBetInfo().addMaxHandler(new CreateRouletteBetMaxHandler());
        roulette.addHowToPlayHandler(new CreateRouletteHowToPlayHandler());
        coinflip.getBettingInfo().addCreateGameEventHandler(new CreateCoinflipCreateGameHandler());
        coinflip.getBettingInfo().addMaxHandler(new CreateCoinflipMaxHandler());
        coinflip.addHowToPlayHandler(new CreateCoinflipHowToPlayHandler());
        chat.getChatBox().addSendHandler(new CreateChatSendHandler());
        accountDetails.addToLoginHandler(new CreateToLoginHandler());
        accountDetails.addToRegisterHandler(new CreateToRegisterHandler());
        accountDetails.getRegisterPage().addRegisterHandler(new CreateRegisterHandler());
        accountDetails.getLoginPage().addLoginHandler(new CreateLoginHandler());
        view.addBackHandler(new CreateBackHandler());
        view.addLoginHandler(new CreateBorderLoginButtonHandler());
        view.addProfileHandler(new CreateToProfileHandler());

    }

    private Game[] getGamesFromserver(){
        Game[] games = new Game[2];

        ///
        games[0] = new Game("Roulette");
        games[1] = new Game("Coinflip");
        ///

        return games;
    }

    private class ComboBoxHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            //check logged in border
            if(view.getComboChoice().toString().equals("Roulette")){
                view.setCenter(roulette);
            } else {view.setCenter(coinflip);}
        }
    }

    private class CreateBorderLoginButtonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            view.setRegisterBorder();
            view.setCenter(accountDetails);
        }
    }

    private class CreateToRegisterHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            view.getAccountDetails().getVbList().clear();
            view.getAccountDetails().getVbList().addAll(view.getAccountDetails().getRegisterPage(), view.getAccountDetails().getToLogin());
        }
    }

    private class CreateToLoginHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            view.getAccountDetails().getVbList().clear();
            view.getAccountDetails().getVbList().addAll(view.getAccountDetails().getLoginPage(), view.getAccountDetails().getToRegister());
        }
    }

    private class CreateToProfileHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            view.setProfileBorder();
            view.setCenter(profile);
        }
    }

    private class CreateLoginHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            String username = view.getAccountDetails().getLoginPage().getUsername();
            String password = view.getAccountDetails().getLoginPage().getPassword();

            MessageDigest md = null;

            try {
                md = MessageDigest.getInstance("MD5");

            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();

            }
            //https://stackoverflow.com/a/30119004
            String hashedPass = String.format("%032x", new BigInteger(1,md.digest(password.getBytes(StandardCharsets.UTF_8))));

            Account existCheck = server.getAccount(username);

            if (existCheck == null){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username or password incorrect");

            } else if (existCheck.getUsername().equals("")) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username or password incorrect");

            } else if (!hashedPass.equals(existCheck.getPassword())) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username or password incorrect");

            } else if (existCheck.getBan()){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "This account has been banned!");

            } else {
                //check role
                view.setLoginBorder(existCheck);

                if(view.getComboChoice().toString().equals("Roulette")){
                    view.setCenter(roulette);
                } else {
                    view.setCenter(coinflip);
                }

                view.setProfile(existCheck);
                profile = view.getProfile();
                profile.getProfileChangeInfo().addBrowseEventHandler(new CreateProfileBrowseHandler());
                profile.getProfileChangeInfo().addUpdateEventHandler(new CreateProfileUpdateHandler());

                if(existCheck.getAccountType().equals("Mod")){
                    chat.getChatMessages().refreshMod();

                } else if (existCheck.getAccountType().equals("Admin")){
                    chat.getChatMessages().refreshMod();
                    profile.getProfileAdminInfo().populateTable(server.getAccount());
                    roulette.getRollInfo().addAdmin();
                    coinflip.getOpenGames().refreshAdmin();

                }
            }
        }
    }

    private class CreateBackHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            if(view.getAccount().getUsername().equals("")){
                view.setLoggedOutBorder();
            } else {
                view.setLoginBorder();
            }

            if(view.getComboChoice().toString().equals("Roulette")){
                view.setCenter(roulette);

            } else {
                view.setCenter(coinflip);

            }
        }
    }

    private class CreateRegisterHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            String username = view.getAccountDetails().getRegisterPage().getUsername();
            String password = view.getAccountDetails().getRegisterPage().getPassword();
            String confirmPassword = view.getAccountDetails().getRegisterPage().getConfirmPassword();
            Account uniqueCheck = server.getAccount(username);
            String specialChar = "!#$%&'()*+,/:\";<=>?@[/]^`{}~ ";
            Boolean specialCheck = false;

            //hash password
            //https://stackoverflow.com/a/415971

            MessageDigest md = null;

            try {
                md = MessageDigest.getInstance("MD5");

            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();

            }

            //https://stackoverflow.com/a/30119004
            String hashedPass = String.format("%032x", new BigInteger(1,md.digest(password.getBytes(StandardCharsets.UTF_8))));

            for (char i: specialChar.toCharArray()){
                if (username.indexOf(i) >= 0){
                    specialCheck = true;
                }
            }

            if (!uniqueCheck.getUsername().equals("")) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username already exists");

            } else if (username.length() < 3){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username must have more than 2 characters");

            } else if (specialCheck.equals(true)){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Username contains illegal characters ('.' and '_' are allowed)");

            } else if (password.length() < 7){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Password must contain more than 7 characters");

            } else if (!password.equals(confirmPassword)) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Passwords do not match");

            } else {
                Account acc = new Account(username, hashedPass);
                server.registerAccount(acc);
                view.getAccountDetails().getVbList().clear();
                view.getAccountDetails().getVbList().addAll(view.getAccountDetails().getLoginPage(), view.getAccountDetails().getToRegister());
            }
        }
    }

    private class CreateRouletteBetHandlerRed implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            //Check login
            RouletteBet bet = createRouletteBet("red");
            if (bet != null) {
                //Account account = server.getAccount(bet.getUserId());
                //view.getRoulette().getBetInfo().getCurrentBetInfoRed().addRecord(bet.getUserId(), bet.getMoneyBet().toString(), account.getProfilePicture());
                server.broadcastRefreshRouletteBets();
            }
        }
    }

    private class CreateRouletteBetHandlerGreen implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            //Check login
            RouletteBet bet = createRouletteBet("green");
            if (bet != null) {
                server.broadcastRefreshRouletteBets();
            }
        }
    }

    private class CreateRouletteBetHandlerBlack implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            //Check login
            RouletteBet bet = createRouletteBet("black");

            if (bet != null) {
                server.broadcastRefreshRouletteBets();
            }
        }
    }

    private class CreateRouletteBetMaxHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            Account account = server.getAccount(view.getAccount().getUsername());
            if(account.getUsername().equals("")){
                roulette.getBetInfo().setRouletteBettingMoney((float) 0);
            } else {
                roulette.getBetInfo().setRouletteBettingMoney(view.getAccount().getMoney());
            }
        }
    }

    private class CreateRouletteHowToPlayHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            String content = "Place a bet:\nTo place a bet, enter a bet or use the buttons to make a bet \n" +
                    "once satified use the 'Bet Red', 'Bet Green' or 'Bet Black' buttons to place your bet!";
            alertDialogBuilder(Alert.AlertType.INFORMATION, "How to play", null, content);
        }
    }

    private class CreateCoinflipCreateGameHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            Account account = server.getAccount(view.getAccount().getUsername());

            try{
                Float betMoney = view.getCoinflip().getBettingInfo().getMoneyToBet();
                Float newMoney = account.getMoney() - betMoney;

                if (account.getUsername().equals("")){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You must login before using this feature");

                } else if (betMoney <= (float) 0){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You cannot bet $0");

                } else if (account.getMoney() < betMoney){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You lack the required funds to place this bet");

                } else{
                    CoinflipGame game = new CoinflipGame(account.getUsername(), betMoney);
                    if(server.createCoinflipGame(game)){
                        server.updateProfileMoney(account, newMoney);
                        view.updateMoney(newMoney);

                    }
                }

            } catch(ClassCastException exc){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Invalid input");

            }
        }
    }

    //https://stackoverflow.com/a/54681379
    public static class CreateCoinflipJoinGameHandler implements EventHandler<ActionEvent>{
        private final CoinflipGame game;

        public CreateCoinflipJoinGameHandler(CoinflipGame game){
            this.game = game;
        }

        public void handle(ActionEvent e){
            Account account = server.getAccount(view.getAccount().getUsername());
            CoinflipGame latestGame = server.getCoinflipGame(game.getId());
            Float newMoney = account.getMoney() - game.getBet();

            if (account.getUsername().equals("")){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You must login before using this feature");

            } else if (latestGame.getPlayerTwoId() != null){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "This game is full");

            } else if (account.getUsername().equals(game.getPlayerOneId())){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You cannot join your own game!");

            } else if (newMoney < 0){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You lack the required funds to place this bet");

            }

            else {
                //retrieve game and update server with new player
                server.updateCoinflipPlayerTwo(account.getUsername(), game);
                server.updateProfileMoney(account, newMoney);
                view.updateMoney(newMoney);
            }
        }
    }

    private class CreateCoinflipHowToPlayHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e) {
            String content = "Creating a game:\nTo create a Coinflip game, enter a bet or use the buttons to make a bet" +
                    "that suits you. Then when you are ready, click the 'Create Game' button. \n\n" +
                    "Joining a game:\nTo join a game, simply find a game you want to join and click the 'Join' button" +
                    "to join the game.";
            alertDialogBuilder(Alert.AlertType.INFORMATION, "How to play", null, content);
        }
    }

    private class CreateCoinflipMaxHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            Account account = server.getAccount(view.getAccount().getUsername());
            if(account.getUsername().equals("")){
                coinflip.getBettingInfo().setMoneyToBet((float) 0);
            } else {
                coinflip.getBettingInfo().setMoneyToBet(view.getAccount().getMoney());
            }
        }
    }

    private class CreateChatSendHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            Account account = server.getAccount(view.getAccount().getUsername());
            Date timeout = account.getTimeout();
            Timestamp now = new Timestamp(new Date().getTime());

            if (account.getUsername().equals("")){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You must login before using this feature");
                        //returns null val
            } else if (account.getTimeout() != null){
                if (account.getTimeout().getTime() > new Date().getTime()) {
                    Date remaining = new Date(timeout.getTime() - now.getTime());

                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You are currently timed out from using this feature ("
                            + remaining.getMinutes() + " minutes remaining)");

                } else {
                    server.createChatMessage(new ChatMessage(account.getUsername(), chat.getChatBox().getTxtMessage().getText()));
                    server.broadcastRefreshChat();
                    chat.getChatBox().getTxtMessage().setText("");

                }

            } else {
                server.createChatMessage(new ChatMessage(account.getUsername(), chat.getChatBox().getTxtMessage().getText()));
                server.broadcastRefreshChat();
                chat.getChatBox().getTxtMessage().setText("");
            }
        }
    }

    private class CreateProfileBrowseHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            //https://stackoverflow.com/a/20961506
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a Picture");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fileChooser.showOpenDialog(new Stage());

            if(file != null){
                try {
                    BufferedImage bImage = ImageIO.read(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    String extension;
                    if(file.toString().toLowerCase().contains(".png")){
                        extension = "png";
                    } else{
                        extension = "jpg";
                    }
                    ImageIO.write(bImage, extension, bos);
                    bos.close();

                    profile.getProfileChangeInfo().setProfilePicturePath(file.toPath().toString());
                    profile.getProfileChangeInfo().setImage(bos.toByteArray());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class CreateProfileUpdateHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            //password
            //profile picture
            byte[] image = profile.getProfileChangeInfo().getImage();
            String oldPassword = profile.getProfileChangeInfo().getOldPassword();
            String newPassword = profile.getProfileChangeInfo().getNewPassword();
            String confNewPassword = profile.getProfileChangeInfo().getNewPassword2();

            if(image != null){
                server.updateProfilePicture(view.getAccount(), image);
                view.refreshIcons(image);
                profile.getProfileChangeInfo().setProfilePicturePath("");
                profile.getProfileChangeInfo().setImage(null);
                alertDialogBuilder(Alert.AlertType.CONFIRMATION, "Confirmation Dialog", null, "Picture Uploaded Successfully!");

            }

            if(!oldPassword.equals("")) {
                MessageDigest md = null;

                try {
                    md = MessageDigest.getInstance("MD5");

                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();

                }
                //https://stackoverflow.com/a/30119004
                String hashedNewPass = String.format("%032x", new BigInteger(1,md.digest(newPassword.getBytes(StandardCharsets.UTF_8))));
                String hashedOldPass = String.format("%032x", new BigInteger(1,md.digest(oldPassword.getBytes(StandardCharsets.UTF_8))));

                if(!view.getAccount().getPassword().equals(hashedOldPass)){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Old password is incorrect!");

                } else if(newPassword.equals("")){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "New password cannot be empty!");

                } else if (newPassword.length() < 7) {
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Password must contain more than 7 characters");

                }else if(newPassword.equals(oldPassword)){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "New password cannot match old password!");

                } else if(!newPassword.equals(confNewPassword)){
                    alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "New passwords do not match!");

                } else {
                    server.updateProfilePassword(view.getAccount(), hashedNewPass);
                    alertDialogBuilder(Alert.AlertType.CONFIRMATION, "Confirmation Dialog", null, "Password has been updated!");
                }
            }
        }
    }

    private RouletteBet createRouletteBet(String colour) {
        RouletteRoll roll = server.getRouletteRoll();

        try {
            Float moneyBet = roulette.getBetInfo().getRouletteBettingMoney();
            Account account = server.getAccount(view.getAccount().getUsername());
            Float newMoney = account.getMoney() - moneyBet;

            if (account.getUsername().equals("")) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You must login before using this feature");
                return null;

            } else if (moneyBet <= (float) 0) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You cannot bet $0");
                return null;

            } else if (account.getMoney() < moneyBet) {
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You lack the required funds to place this bet");
                return null;

            } else if (roulette.getRollInfo().getTimer().equals("0")){
                alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "You cannot bet when rolling!");
                return null;

            } else {
                //ROLL ID NEEDS TO BE LINKED TO ACTUAL ROLL //Addressed
                RouletteBet bet = new RouletteBet(roll.getRollId(), account.getUsername(), moneyBet, colour);
                if (server.createRouletteBet(bet)) {
                    server.updateProfileMoney(account, newMoney);
                    view.updateMoney(newMoney);
                    return bet;
                    //MONEY BROKEN WHEN BETTING //Addressed

                } else {
                    return null;

                }
            }

        } catch (ClassCastException exc) {
            alertDialogBuilder(Alert.AlertType.ERROR, "Error Dialog", null, "Invalid input");
            return null;

        }
    }

    private class MessageHandler extends Thread {
        public void run() {
            LinkedBlockingQueue<Object> messages = server.getMessagesForMsgHandler();
            Object line;

            while (true) {
                try {
                    line = server.getMessagesForMsgHandler().take();
                    if (line.equals("ROULETTETIMER")) {
                        String i = (String) messages.take();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                roulette.getRollInfo().updateTimer(new Integer(i));
                            }
                        });

                    } else if (line.equals("ROULETTECLEAR")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                roulette.getBetInfo().getCurrentBetInfoBlack().clearRecords();
                                roulette.getBetInfo().getCurrentBetInfoGreen().clearRecords();
                                roulette.getBetInfo().getCurrentBetInfoRed().clearRecords();
                            }
                        });


                    } else if (line.equals("ROULETTEGIVECOLOUR")) {
                        String a = (String) messages.take();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                roulette.getRollInfo().updateColour(a);

                            }
                        });


                    } else if (line.equals("ROULETTEUPDATEPREVROLLS")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                roulette.getRollInfo().updatePrevRolls();
                            }
                        });


                    } else if (line.equals("COINFLIPREFRESH")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (view.getAccount().getAccountType().equals("Admin")){
                                    coinflip.getOpenGames().refreshAdmin();

                                }else {
                                    coinflip.getOpenGames().refresh();

                                }
                            }
                        });


                    } else if (line.equals("COINFLIPSETWINNER")) {
                        CoinflipGame i = (CoinflipGame) messages.take();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    coinflip.getOpenGames().setWinner(i, view);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();

                                }
                            }
                        });

                    } else if (line.equals("MONEYREFRESH")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (!view.getAccount().getUsername().equals("")){
                                    view.updateMoney();
                                }
                            }
                        });

                    } else if (line.equals("CHATREFRESH")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (view.getAccount().getAccountType().equals("Mod") || view.getAccount().getAccountType().equals("Admin")) {
                                    chat.getChatMessages().refreshMod();

                                } else {
                                    chat.getChatMessages().refresh();

                                }
                            }
                        });

                    }else if (line.equals("ROULETTEBETREFRESH")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                roulette.getBetInfo().refreshBets();

                            }
                        });
                    } else {
                        System.out.println("COMMAND UNRECOGNISED" + line);
                        //messages.put(line);
                        //messages.addLast(line);
                        //out.writeObject("Command Unrecognised");
                        //out.flush();
                    }

                } catch (InterruptedException | IllegalMonitorStateException e) {
                    e.printStackTrace();
                    this.stop();
                    return;
                }
            }
        }
    }

    public MessageHandler getMessageHandler(){
        return messageHandler;
    }

    private static void alertDialogBuilder(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
