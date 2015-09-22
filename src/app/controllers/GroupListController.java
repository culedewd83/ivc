package app.controllers;

import app.Main;
import app.panes.StartPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Created by jesse on 9/21/15.
 */
public class GroupListController implements IBaseController {

    @FXML
    Label greetingLbl;

    @FXML
    Button logoutBtn;

    @FXML
    ListView groupListView;

    @Override
    public void init() {

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.getInstance().setProfile(null);
                Main.getInstance().setPane(new StartPane(), true, true);
            }
        });

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);
    }
}
