package app.controllers;

import app.Main;
import app.models.TemplateGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Created by jesse on 9/21/15.
 */
public class GroupController implements IBaseController {

    @FXML
    Label greetingLbl;

    @FXML
    Label groupNameLbl;

    @FXML
    Pane warningPane;

    @FXML
    TextField groupName;

    @FXML
    Button logoutBtn;

    @FXML
    Button sendBtn;

    @FXML
    Button editBtn;

    @FXML
    Button backBtn;

    @FXML
    Button saveBtn;

    private TemplateGroup mGroup;

    @Override
    public void init() {

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logoutBtnClicked();
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              backBtnClicked();
            }
        });

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);

        mGroup = Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex());

        groupName.setText(mGroup.groupName);
    }


    private void logoutBtnClicked() {
//        Main.getInstance().setProfile(null);
//        Main.getInstance().setPane(new StartPane(), true, true);
    }

    private void backBtnClicked() {
        Main.getInstance().popPaneStack();
    }
}
