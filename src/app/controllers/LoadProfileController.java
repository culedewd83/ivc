package app.controllers;

import app.Main;
import app.panes.GroupListPane;
import app.rest.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jesse on 9/21/15.
 */
public class LoadProfileController implements IBaseController, IResponse {

    @FXML
    Button loadBtn;

    @FXML
    Button cancelBtn;

    @FXML
    Pane aNumberWarning;

    @FXML
    Label aNumberLbl;

    @FXML
    TextField aNumber;

    @FXML
    AnchorPane busyPane;


    boolean isBusy = false;
    String mKey;

    public LoadProfileController() {
        this("");
    }

    public LoadProfileController(String key) {
        mKey = key;
    }

    @Override
    public void init() {
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadButtonClicked();
            }
        });

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelButtonClicked();
            }
        });

        aNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                hideANumbWarning();
            }
        });

        aNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    aNumber.setText(aNumber.getText().trim());
                }
            }
        });

        busyPane.toFront();

        if (mKey != null && mKey.length() > 0) {
            aNumber.setText(mKey);
            loadBtn.fire();
        }
    }

    @Override
    public void onResponse(BasicResponse response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                isBusy = false;
                busyPane.setVisible(false);
                aNumber.setDisable(false);
                cancelBtn.setDisable(false);
                loadBtn.setDisable(false);

                if (response.success) {
                    Main.getInstance().setProfile(((ProfileResponse)response).profile);
                    Main.getInstance().popPaneStack();
                    Main.getInstance().setPane(new GroupListPane(), true, false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Load Profile Error");
                    alert.setContentText(response.message);

                    alert.showAndWait();
                }
            }
        });
    }

    private void loadButtonClicked() {
        loadProfile();
    }

    private void cancelButtonClicked() {
        Main.getInstance().popPaneStack();
    }

    private void showANumbWarning() {
        aNumberLbl.setText("Invalid A-Number");
        aNumberWarning.setVisible(true);
    }

    private void hideANumbWarning() {
        aNumberLbl.setText("A-Number");
        aNumberWarning.setVisible(false);
    }

    private void loadProfile() {
        boolean isValid = true;
        String key = "";

        String pattern = "^[aA]?(\\d{8})$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(aNumber.getText().trim());
        if (m.find()) {
            key = m.group(1);
        } else {
            isValid = false;
            showANumbWarning();
        }

        if (isValid) {
            aNumber.setDisable(true);
            cancelBtn.setDisable(true);
            loadBtn.setDisable(true);
            isBusy = true;
            busyPane.setVisible(true);
            GetProfileRequest.getProfile(key, this);
        }
    }
}
