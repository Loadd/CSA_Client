package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ChatBox extends HBox {
    private TextField txtMessage;
    private Button btnSend;

    public ChatBox() {
        txtMessage = new TextField();
        btnSend = new Button("Send");

        this.getChildren().addAll(txtMessage, btnSend);
    }

    public TextField getTxtMessage() {return txtMessage;}

    public void addSendHandler(EventHandler<ActionEvent> handler){
        btnSend.setOnAction(handler);
    }

}
