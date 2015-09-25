package app.controllers;

import app.Main;
import app.models.TemplateGroup;
import app.panes.StartPane;
import app.panes.TemplateListPane;
import app.rest.BasicResponse;
import app.rest.IResponse;
import app.rest.SaveProfileRequest;
import app.utils.Copy;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.Optional;

/**
 * Created by jesse on 9/21/15.
 */
public class GroupController implements IBaseController, IResponse {

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

    @FXML
    AnchorPane busyPane;

    private TemplateGroup mGroup;
    private TemplateGroup oldGroup;
    private boolean needSave = false;
    private boolean isLogout = false;
    private boolean isBack = false;
    private boolean isSend = false;
    private boolean isEdit = false;
    private boolean isSave = false;

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

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendBtnClicked();
            }
        });

        editBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editBtnClicked();
            }
        });

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveBtnClicked();
            }
        });

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);
        mGroup = Copy.copyGroup(Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()));
        groupName.setText(mGroup.groupName);
        busyPane.toFront();

        groupName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
                sendBtn.setDisable(true);
                editBtn.setDisable(true);
                hideNameWarning();
            }
        });
    }


    private void logoutBtnClicked() {

        if (needSave) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Logout");
            alert.setContentText("Save before logging out?");

            ButtonType buttonTypeOne = new ButtonType("Cancel");
            ButtonType buttonTypeTwo = new ButtonType("Don't Save");
            ButtonType buttonTypeThree = new ButtonType("Save");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                // ... user chose CANCEL or closed the dialog
            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Don't Save"
                Main.getInstance().setGroupIndex(-1);
                Main.getInstance().setProfile(null);
                Main.getInstance().setPane(new StartPane(), true, true);
            } else if (result.get() == buttonTypeThree) {
                // ... user chose "Three"
                isLogout = true;
                saveGroup();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Logout");
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Main.getInstance().setGroupIndex(-1);
                Main.getInstance().setProfile(null);
                Main.getInstance().setPane(new StartPane(), true, true);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    private void backBtnClicked() {

        if (needSave) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Back Confirmation");
            alert.setHeaderText("Go Back");
            alert.setContentText("Save before going back?");

            ButtonType buttonTypeOne = new ButtonType("Cancel");
            ButtonType buttonTypeTwo = new ButtonType("Don't Save");
            ButtonType buttonTypeThree = new ButtonType("Save");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                // ... user chose CANCEL or closed the dialog
            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Don't Save"
                Main.getInstance().popPaneStack();
            } else if (result.get() == buttonTypeThree) {
                // ... user chose "Three"
                isBack = true;
                saveGroup();
            }
        } else {
            Main.getInstance().popPaneStack();
        }
    }

    private void sendBtnClicked() {

    }

    private void editBtnClicked() {
        Main.getInstance().setPane(new TemplateListPane(), true, false);
    }

    private void saveBtnClicked() {
        isSave = true;
        saveGroup();
    }

    @Override
    public void onResponse(BasicResponse response) {
        if (response.success) {
            needSave = false;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (isLogout) {
                        Main.getInstance().setGroupIndex(-1);
                        Main.getInstance().setProfile(null);
                        Main.getInstance().setPane(new StartPane(), true, true);
                    } else if (isSave) {
                        hideBusyWheel();
                    } else if (isBack) {
                        Main.getInstance().popPaneStack();
                    }
                }
            });
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.getInstance().getProfile().groups.set(Main.getInstance().getGroupIndex(), oldGroup);
                    hideBusyWheel();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Save Error");
                    alert.setContentText("An error occurred while saving");
                    alert.showAndWait();
                }
            });
        }
    }

    private void saveGroup() {
        String name = groupName.getText().trim();
        groupName.setText(name);

        int i = 0;
        for (TemplateGroup group : Main.getInstance().getProfile().groups) {
            if (i != Main.getInstance().getGroupIndex() && group.groupName.equals(name)) {
                hideBusyWheel();
                showNameWarning("Group Name Already Exists");
                return;
            }
            i++;
        }

        if (name.length() > 0) {
            showBusyWheel();
            oldGroup = Copy.copyGroup(Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()));
            mGroup.groupName = name;
            Main.getInstance().getProfile().groups.set(Main.getInstance().getGroupIndex(), mGroup);
            SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
        } else {
            hideBusyWheel();
            showNameWarning("Empty Group Name");
        }
    }

    private void showNameWarning(String msg) {
        groupNameLbl.setText(msg);
        warningPane.setVisible(true);
    }

    private void hideNameWarning() {
        groupNameLbl.setText("Group Name");
        warningPane.setVisible(false);
    }

    private void showBusyWheel() {
        logoutBtn.setDisable(true);
        groupName.setDisable(true);
        sendBtn.setDisable(true);
        editBtn.setDisable(true);
        saveBtn.setDisable(true);
        busyPane.setVisible(true);
    }

    private void hideBusyWheel() {
        isLogout = false;
        isBack = false;
        isSave = false;
        isSend = false;
        isEdit = false;
        logoutBtn.setDisable(false);
        groupName.setDisable(false);
        sendBtn.setDisable(needSave);
        editBtn.setDisable(needSave);
        saveBtn.setDisable(!needSave);
        busyPane.setVisible(false);
    }
}
