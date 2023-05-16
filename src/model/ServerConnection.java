package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnection {
    private String ip = "localhost";
    private Integer port = 2222;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private LinkedBlockingQueue<Object> messages;
    private LinkedBlockingQueue<Object> messagesForMsgHandler;
    private MessageReciever rec;

    public void startConnection(){
        try {
            Socket clientSocket = new Socket(ip, port);
            messages = new LinkedBlockingQueue<>();
            messagesForMsgHandler = new LinkedBlockingQueue<>();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            rec = new MessageReciever();
            rec.start();

        }catch (IOException e){
            e.printStackTrace();

        }
    }

    private class MessageReciever extends Thread {
        public void run(){
            ArrayList<String> noParams = new ArrayList<>(Arrays.asList("ROULETTECLEAR", "ROULETTEUPDATEPREVROLLS", "COINFLIPREFRESH", "MONEYREFRESH", "CHATREFRESH", "ROULETTEBETREFRESH"));
            ArrayList<String> params = new ArrayList<>(Arrays.asList("ROULETTETIMER", "ROULETTEGIVECOLOUR", "COINFLIPSETWINNER"));

            while (true) {
                try{
                    Object message = in.readObject();

                    try {
                        String line = (String) message;
                        if (noParams.contains(line)){
                            System.out.println(line);
                            messagesForMsgHandler.put(line);

                        } else if (params.contains(line)){
                            System.out.println(line);
                            messagesForMsgHandler.put(line);
                            messagesForMsgHandler.put(in.readObject());

                        } else {
                            System.out.println("E");
                            messages.put(message);
                        }
                    } catch (ClassCastException exc){
                        //exc.printStackTrace();
                        System.out.println("T");
                        messages.put(message);
                    }


                } catch (IOException | InterruptedException | ClassNotFoundException e){
                    e.printStackTrace();
                    this.stop();
                    disconnect();
                }
            }
        }
    }

    public Thread getReceiver(){
        return rec;
    }

    public String checkServerConnection(){
        try{
            System.out.println("2");
            out.writeObject("HELLO");
            System.out.println("1");
            out.flush();
            System.out.println("24");
            String e = (String) messages.take();
            System.out.println("3");
            return e;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public void disconnect(){
        try{
            out.writeObject("QUIT");
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public RouletteRoll getRouletteRoll() {
        try{
            System.out.println("3");
            out.writeObject("GETROULETTEROLL");
            out.flush();
            RouletteRoll resp = (RouletteRoll) messages.take();
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public RouletteRoll getRouletteRollFromId(Integer id) {
        try{
            System.out.println("4");
            out.writeObject("GETROULETTEROLLFROMID");
            out.writeObject(id);
            out.flush();
            RouletteRoll resp = (RouletteRoll) messages.take();
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<RouletteRoll> getRouletteRollPrevious() {
        try{
            System.out.println("5");
            out.writeObject("GETROULETTEROLLPREVIOUS");
            out.flush();
            ArrayList<RouletteRoll> resp = (ArrayList<RouletteRoll>) messages.take();

            return resp;

        } catch (IOException | InterruptedException | ClassCastException e){
            e.printStackTrace();
            return null;

        }
    }

    public RouletteBet getRouletteBet(Integer i) {
        try{
            out.writeObject("GETROULETTEBET");
            out.writeObject(i);
            out.flush();
            //RouletteBet resp = (RouletteBet) in.readObject();
            RouletteBet resp = (RouletteBet) messages.take();
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<RouletteBet> getRouletteBets(String i) {
        try{
            System.out.println("6");
            out.writeObject("GETROULETTEBETS");
            out.writeObject(i);
            out.flush();
            //ArrayList<RouletteBet> resp = (ArrayList<RouletteBet>) in.readObject();
            ArrayList<RouletteBet> resp = (ArrayList<RouletteBet>) messages.take();
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<RouletteBet> getRouletteBets(Integer i, String j) {
        try{
            System.out.println("Asking for RouletteBets!");
            out.writeObject("GETROULETTEBETS2");
            System.out.println("Ask Accepted!");
            System.out.println("Sending Id...");
            out.writeObject(i);
            System.out.println("Roll Id Sent!");
            System.out.println("Sending colour...");
            out.writeObject(j);
            out.flush();
            System.out.println("Colour Sent!");
            System.out.println("Waitng for response...");
            //ArrayList<RouletteBet> resp = (ArrayList<RouletteBet>) in.readObject();
            ArrayList<RouletteBet> resp = (ArrayList<RouletteBet>) messages.take();
            System.out.println("Response recieved!");
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public CoinflipGame getCoinflipGame(Integer id){
        try{
            System.out.println("7");
            out.writeObject("GETCOINFLIPGAME");
            out.writeObject(id);
            out.flush();
            CoinflipGame resp = (CoinflipGame) messages.take();
            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<CoinflipGame> getCoinflipGames(String i) {
        try{
            System.out.println("9");
            out.writeObject("GETCOINFLIPGAMES");
            out.writeObject(i);
            out.flush();
            ArrayList<CoinflipGame> resp = (ArrayList<CoinflipGame>) messages.take();

            return resp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public CoinflipGame getLatestCoinflipGame(String id) {
        try{
            out.writeObject("GETLATESTCOINFLIPGAME");
            out.writeObject(id);
            out.flush();
            //return (CoinflipGame) in.readObject();
            return (CoinflipGame) messages.take();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<CoinflipGame> getOpenCoinflipGames() {
        try{
            System.out.println("10");
            out.writeObject("GETOPENCOINFLIPGAMES");
            out.flush();
            ArrayList<CoinflipGame> resp = (ArrayList<CoinflipGame>) messages.take();

            return resp;

        } catch (IOException | InterruptedException | ClassCastException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<Account> getAccount() {
        try{
            System.out.println("11");
            out.writeObject("GETACCOUNT");
            out.flush();
            //return (ArrayList<Account>) in.readObject();
            return (ArrayList<Account>) messages.take();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return null;

        }
    }

    public Account getAccount(String i) {
        try{
            System.out.println("12");
            out.writeObject("GETACCOUNT2");
            out.writeObject(i);
            out.flush();
            return (Account) messages.take();

        } catch (IOException | InterruptedException | ClassCastException e){
            e.printStackTrace();
            return null;

        }
    }

    public ArrayList<ChatMessage> getChatMessages() {
        try{
            System.out.println("28");
            out.writeObject("GETCHATMESSAGES");
            out.flush();
            return (ArrayList<ChatMessage>) messages.take();

        } catch (IOException | InterruptedException | ClassCastException e){
            e.printStackTrace();
            return null;

        }
    }

    public void registerAccount(Account i) {
        try{
            System.out.println("13");
            out.writeObject("REGISTERACCOUNT");
            out.writeObject(i);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public boolean createRouletteRoll() {
        try{
            out.writeObject("CREATEROULETTEROLL");
            out.flush();
            return true;

        } catch (IOException e){
            e.printStackTrace();
            return false;

        }
    }

    public Boolean createCoinflipGame(CoinflipGame i) {
        try{
            System.out.println("14");
            out.writeObject("CREATECOINFLIPGAME");
            out.writeObject(i);
            out.flush();
            return true;

        } catch (IOException e){
            e.printStackTrace();
            return false;

        }
    }

    public boolean createRouletteBet(RouletteBet i) {
        try{
            System.out.println("15");
            out.writeObject("CREATEROULETTEBET");
            out.writeObject(i);
            out.flush();
            return true;

        } catch (IOException e){
            e.printStackTrace();
            return false;

        }
    }

    public void createChatMessage(ChatMessage i) {
        try{
            System.out.println("16");
            out.writeObject("CREATECHATMESSAGE");
            out.writeObject(i);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateRouletteRoll(Integer id, String colour) {
        try{
            System.out.println("Asking for Update Roulette Roll!");
            out.writeObject("UPDATEROULETTEROLL");
            System.out.println("Ask Accepted!");
            System.out.println("Sending ID...");
            out.writeObject(id);
            System.out.println("ID SENT!");
            System.out.println("Sending colour...");
            out.writeObject(colour);
            System.out.println("Colour sent!");
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateCoinflipPlayerTwo(String playerTwoId, CoinflipGame game){
        try{
            System.out.println("17");
            out.writeObject("UPDATECOINFLIPPLAYERTWO");
            out.writeObject(playerTwoId);
            out.writeObject(game);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateProfilePicture(Account i, byte[] profilePicture) {
        try{
            System.out.println("18");
            out.writeObject("UPDATEPROFILEPICTURE");
            out.writeObject(i);
            out.writeObject(profilePicture);
            out.flush();

        } catch (IOException  e){
            e.printStackTrace();

        }
    }

    public void updateProfilePassword(Account i, String newPassword) {
        try{
            System.out.println("19");
            out.writeObject("UPDATEPROFILEPASSWORD");
            out.writeObject(i);
            out.writeObject(newPassword);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateProfileMoney(Account i, Float newMoney) {
        try{
            System.out.println("20");
            out.writeObject("UPDATEPROFILEMONEY");
            out.writeObject(i);
            out.writeObject(newMoney);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateProfileType(Account i, String j){
        try{
            System.out.println("21");
            out.writeObject("UPDATEPROFILETYPE");
            out.writeObject(i);
            out.writeObject(j);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void updateProfileBan(Account i, Boolean newBan) {
        try{
            System.out.println("22");
            out.writeObject("UPDATEPROFILEBAN");
            out.writeObject(i);
            out.writeObject(newBan);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void deleteChatMessage(ChatMessage i, Boolean newRemove) {
        try{
            System.out.println("23");
            out.writeObject("DELETECHATMESSAGE");
            out.writeObject(i);
            out.writeObject(newRemove);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void broadcastRefreshChat(){
        try{
            System.out.println("24");
            out.writeObject("CHATREFRESH");
            out.flush();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void broadcastRefreshRouletteBets(){
        try{
            System.out.println("28");
            out.writeObject("ROULETTEBETREFRESH");
            out.flush();

        } catch (IOException e){
            e.printStackTrace();
        }
    }



    public void timeoutAccount(Account i) {
        try{
            System.out.println("25");
            out.writeObject("TIMEOUTACCOUNT");
            out.writeObject(i);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void forceCoinflipWinner(CoinflipGame i, Account j) {
        try{
            System.out.println("26");
            out.writeObject("FORCECOINFLIPWINNER");
            out.writeObject(i);
            out.writeObject(j);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    public void overrideRouletteColour(RouletteRoll i, String j) {
        try{
            System.out.println("27");
            out.writeObject("OVERRIDEROULETTECOLOUR");
            out.writeObject(i);
            out.writeObject(j);
            out.flush();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    //public LinkedBlockingQueue<Object> getMessages() {return messages;}
    public LinkedBlockingQueue<Object> getMessages() {return messages;}
    public LinkedBlockingQueue<Object> getMessagesForMsgHandler() {return messagesForMsgHandler;}
}
