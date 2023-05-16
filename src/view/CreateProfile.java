package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Account;
import model.ServerConnection;

public class CreateProfile extends HBox {
    private ProfileDisplay profileDisplay;
    private ProfileChangeInfo profileChangeInfo;
    //private VBox profileChangeInfo;
    private ProfileStats profileStats;
    private ProfileAdminInfo profileAdminInfo;

    public CreateProfile(Account i, ServerConnection server){
        VBox left = new VBox();
        VBox right = new VBox();

        profileDisplay = new ProfileDisplay(i);
        profileChangeInfo = new ProfileChangeInfo(i);

        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        profileStats = new ProfileStats(i, server);
        profileAdminInfo = new ProfileAdminInfo(server);

        Tab t1 = new Tab("Stats", profileStats);
        Tab t2 = new Tab("Users", profileAdminInfo);

        tp.getTabs().add(t1);

        if(i.getAccountType().equalsIgnoreCase("Admin")){
            tp.getTabs().add(t2);
        }

        left.getChildren().addAll(profileDisplay, profileChangeInfo);
        right.getChildren().add(tp);

        left.setAlignment(Pos.TOP_LEFT);
        right.setAlignment(Pos.TOP_RIGHT);

        left.setSpacing(25);

        this.setSpacing(10);
        this.getChildren().addAll(left, right);
        this.setAlignment(Pos.CENTER);
        //this.getChildren().addAll(left);

    }

    public ProfileChangeInfo getProfileChangeInfo(){return profileChangeInfo;}
    public ProfileAdminInfo getProfileAdminInfo(){return profileAdminInfo;}
    public ProfileDisplay getProfileDisplay(){return profileDisplay;}
}
