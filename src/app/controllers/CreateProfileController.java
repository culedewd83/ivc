package app.controllers;

import app.Main;
import app.models.ProfileInfo;
import app.panes.LoadProfilePane;
import app.rest.BasicResponse;
import app.rest.CreateProfileRequest;
import app.rest.IResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jesse on 9/9/15.
 */
public class CreateProfileController implements IBaseController, IResponse, Initializable {

    @FXML
    TextField aNumber;

    @FXML
    Label aNumberLbl;

    @FXML
    Pane aNumberWarning;

    @FXML
    TextField email;

    @FXML
    Label emailLbl;

    @FXML
    Pane emailWarning;

    @FXML
    TextField name;

    @FXML
    Label nameLbl;

    @FXML
    Pane nameWarning;

    @FXML
    Button cancelBtn;

    @FXML
    Button createBtn;

    @FXML
    AnchorPane busyPane;

    boolean isBusy = false;
    private String mKey;

    @Override
    public void init() {

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isBusy) {
                    return;
                }

                Main.getInstance().popPaneStack();
            }
        });

        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isBusy) {
                    return;
                }

                createProfile();
            }
        });

        aNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                hideANumbWarning();
            }
        });

        email.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                hideEmailWarning();
            }
        });

        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                hideNameWarning();
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

        email.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    email.setText(email.getText().trim());
                }
            }
        });

        name.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    name.setText(name.getText().trim());
                }
            }
        });

        busyPane.toFront();
    }

    private void createProfile() {

        boolean isValid = true;
        ProfileInfo info = new ProfileInfo();

        String pattern = "^[aA]?(\\d{8})$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(aNumber.getText().trim());
        if (m.find()) {
            info.aNumber = m.group(1);
            mKey = m.group(1);
        } else {
            isValid = false;
            showANumbWarning();
        }

        pattern = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        r = Pattern.compile(pattern);
        m = r.matcher(email.getText().trim());
        if (m.find()) {
            info.email = email.getText().trim();
        } else {
            isValid = false;
            showEmailWarning();
        }

        if (name.getText().trim().length() > 0) {
            info.name = name.getText().trim();
        } else {
            isValid = false;
            showNameWarning();
        }

        if (isValid) {
            aNumber.setDisable(true);
            email.setDisable(true);
            name.setDisable(true);
            cancelBtn.setDisable(true);
            createBtn.setDisable(true);
            isBusy = true;
            busyPane.setVisible(true);
            CreateProfileRequest.sendRequest(info, this);
        }
    }

    @Override
    public void onResponse(final BasicResponse response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                isBusy = false;
                busyPane.setVisible(false);
                aNumber.setDisable(false);
                email.setDisable(false);
                name.setDisable(false);
                cancelBtn.setDisable(false);
                createBtn.setDisable(false);

                if (response.success) {
                    Main.getInstance().popPaneStack();
                    Main.getInstance().setPane(new LoadProfilePane("A" + mKey), true, false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Profile Creation Error");
                    alert.setContentText(response.message);

                    alert.showAndWait();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        email.requestFocus();
    }

    private void showANumbWarning() {
        aNumberLbl.setText("Invalid A-Number");
        aNumberWarning.setVisible(true);
    }

    private void hideANumbWarning() {
        aNumberLbl.setText("A-Number");
        aNumberWarning.setVisible(false);
    }

    private void showEmailWarning() {
        emailLbl.setText("Invalid Email Address");
        emailWarning.setVisible(true);
    }

    private void hideEmailWarning() {
        emailLbl.setText("Email Address");
        emailWarning.setVisible(false);
    }

    private void showNameWarning() {
        nameLbl.setText("Name Must Not Be Empty");
        nameWarning.setVisible(true);
    }

    private void hideNameWarning() {
        nameLbl.setText("Name");
        nameWarning.setVisible(false);
    }
}
