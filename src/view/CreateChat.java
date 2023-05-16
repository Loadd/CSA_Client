package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import model.ServerConnection;

public class CreateChat extends VBox {
    private ChatMessages chatMessages;
    private ChatBox chatBox;

    public CreateChat(ServerConnection server) {
        chatMessages = new ChatMessages(server);
        chatBox = new ChatBox();

        ObservableList vbList = this.getChildren();
        vbList.addAll(chatMessages, chatBox);

        this.setMinWidth(10);
        this.setMinHeight(10);
        this.setSpacing(2);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setStyle("-fx-border-color: white; -fx-background-color: rgb(23,23,23);");
        this.setAlignment(Pos.BOTTOM_CENTER);

    }

    public ChatMessages getChatMessages(){return chatMessages;}

    public ChatBox getChatBox(){return chatBox;}

}
