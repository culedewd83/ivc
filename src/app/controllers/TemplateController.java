package app.controllers;

import app.Main;
import app.models.ReportTemplate;
import app.panes.StartPane;
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
import javafx.scene.layout.GridPane;

import java.util.Optional;


/**
 * Created by jesse on 9/21/15.
 */
public class TemplateController implements IBaseController, IResponse {

    @FXML
    AnchorPane busyPane;

    @FXML
    Label greetingLbl;

    @FXML
    Button logoutBtn;

    @FXML
    Button backBtn;

    @FXML
    Button saveBtn;

    @FXML
    ScrollPane scrollPane;

    @FXML
    GridPane gridPane;

    @FXML
    TextField name;

    @FXML
    TextField className;

    @FXML
    TextField room;

    @FXML
    TextField origin;

    @FXML
    TextField facilitatorPresent;

    @FXML
    TextField techIssues;

    @FXML
    TextField cancellations;

    @FXML
    TextField assignments;

    @FXML
    TextField facilitiesIssues;

    @FXML
    TextField comments;


    private ReportTemplate mTemplate;
    private ReportTemplate oldTemplate;
    private boolean needSave = false;
    private boolean isLogout = false;
    private boolean isBack = false;
    private boolean isSave = false;
    private int mGroupIndex;
    private int mTemplateIndex;

    @Override
    public void init() {

        mGroupIndex = Main.getInstance().getGroupIndex();
        mTemplateIndex = Main.getInstance().getTemplateIndex();

        gridPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(18.0));

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);

        mTemplate = Copy.copyTemplate(Main.getInstance()
                .getProfile()
                .groups
                .get(mGroupIndex)
                .templates
                .get(mTemplateIndex));

        assignments.setText(mTemplate.assignmentsQuizzesExams);
        cancellations.setText(mTemplate.cancellations);
        comments.setText(mTemplate.comments);
        className.setText(mTemplate.course);
        facilitatorPresent.setText(mTemplate.facilitatorPresent);
        facilitiesIssues.setText(mTemplate.facilitiesIssues);
        name.setText(mTemplate.name);
        origin.setText(mTemplate.origin);
        room.setText(mTemplate.room);
        techIssues.setText(mTemplate.techIssues);

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

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveBtnClicked();
            }
        });

        assignments.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        cancellations.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        comments.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        className.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        facilitatorPresent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        facilitiesIssues.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        origin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        room.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        techIssues.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                needSave = true;
                saveBtn.setDisable(false);
            }
        });

        busyPane.toFront();
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
                saveTemplate();
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
                saveTemplate();
            }
        } else {
            Main.getInstance().popPaneStack();
        }
    }

    private void saveBtnClicked() {
        isSave = true;
        saveTemplate();
    }

    private void trimAll() {
        assignments.setText(assignments.getText().trim());
        cancellations.setText(cancellations.getText().trim());
        comments.setText(comments.getText().trim());
        className.setText(className.getText().trim());
        facilitatorPresent.setText(facilitatorPresent.getText().trim());
        facilitiesIssues.setText(facilitiesIssues.getText().trim());
        name.setText(name.getText().trim());
        origin.setText(origin.getText().trim());
        room.setText(room.getText().trim());
        techIssues.setText(techIssues.getText().trim());
    }

    private void saveTemplate() {
        trimAll();

        String templateName = name.getText();

        if (templateName == null || templateName.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Template Name");
            alert.setContentText("Template name must not be empty");
            alert.showAndWait();
            return;
        }

        int i = 0;
        for (ReportTemplate template : Main.getInstance().getProfile().groups.get(mGroupIndex).templates) {
            if (i != mTemplateIndex && template.name.equals(templateName)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Template Name");
                alert.setContentText("Template name already exists");
                alert.showAndWait();
                return;
            }
            i++;
        }

        showBusyWheel();

        oldTemplate = mTemplate = Copy.copyTemplate(Main.getInstance()
                .getProfile()
                .groups
                .get(mGroupIndex)
                .templates
                .get(mTemplateIndex));

        Main.getInstance()
                .getProfile()
                .groups
                .get(mGroupIndex)
                .templates
                .set(mTemplateIndex, getTemplate());

        SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
    }

    private ReportTemplate getTemplate() {
        ReportTemplate result = new ReportTemplate();
        result.assignmentsQuizzesExams = assignments.getText();
        result.cancellations = cancellations.getText();
        result.comments = comments.getText();
        result.course = className.getText();
        result.facilitatorPresent = facilitatorPresent.getText();
        result.facilitiesIssues = facilitiesIssues.getText();
        result.name = name.getText();
        result.origin = origin.getText();
        result.room = room.getText();
        result.techIssues = techIssues.getText();
        return result;
    }

    private void showBusyWheel() {
        logoutBtn.setDisable(true);
        backBtn.setDisable(true);
        saveBtn.setDisable(true);

        assignments.setEditable(true);
        cancellations.setEditable(true);
        comments.setEditable(true);
        className.setEditable(true);
        facilitatorPresent.setEditable(true);
        facilitiesIssues.setEditable(true);
        name.setEditable(true);
        origin.setEditable(true);
        room.setEditable(true);
        techIssues.setEditable(true);

        busyPane.setVisible(true);
    }

    private void hideBusyWheel() {
        isLogout = false;
        isBack = false;
        isSave = false;

        logoutBtn.setDisable(false);
        backBtn.setDisable(false);
        saveBtn.setDisable(!needSave);

        assignments.setEditable(false);
        cancellations.setEditable(false);
        comments.setEditable(false);
        className.setEditable(false);
        facilitatorPresent.setEditable(false);
        facilitiesIssues.setEditable(false);
        name.setEditable(false);
        origin.setEditable(false);
        room.setEditable(false);
        techIssues.setEditable(false);

        busyPane.setVisible(false);
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
                    Main.getInstance()
                            .getProfile()
                            .groups
                            .get(mGroupIndex)
                            .templates
                            .set(mTemplateIndex, oldTemplate);

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
}
