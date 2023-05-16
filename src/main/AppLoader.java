package main;

import controller.CSAController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ServerConnection;
import view.CreateBorder;

public class AppLoader extends Application {

    private CreateBorder view;
    private ServerConnection server;
    private CSAController controller;

    @Override
    public void init() {
        server = new ServerConnection();
        server.startConnection();

        if (server.checkServerConnection() == null) {
            System.out.println("Please check your connection");

        } else{
            System.out.println("Connection Accepted!");
            view = new CreateBorder(server);
            controller = new CSAController(view, server);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        stage.setTitle("Casino Simulator Application");
        stage.setScene(new Scene(view));
        stage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stopping Application");
        Thread messages = controller.getMessageHandler();
        messages.stop();
        server.disconnect();
        server.getReceiver().stop();

        //stop all threads
    }

    public static void main(String[] args) {
        launch(args);
    }
}

