package app.controllers;

import app.Main;
import app.models.ReportTemplate;
import app.models.TemplateGroup;
import app.panes.GroupPane;
import app.panes.StartPane;
import app.rest.BasicResponse;
import app.rest.IResponse;
import app.rest.SaveProfileRequest;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by jesse on 9/21/15.
 */
public class GroupListController implements IBaseController, IResponse {

    @FXML
    Label greetingLbl;

    @FXML
    Button logoutBtn;

    @FXML
    ListView groupListView;

    @FXML
    Button deleteBtn;

    @FXML
    Button newBtn;

    @FXML
    Button openBtn;

    @FXML
    AnchorPane busyPane;

    @FXML
    Label busyLbl;

    boolean isDelete = false;
    boolean isNew = false;
    TemplateGroup deleteGroup;
    int deleteIndex;

    @Override
    public void init() {

        groupListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() >= 0) {
                    deleteBtn.setDisable(false);
                    openBtn.setDisable(false);
                } else {
                    deleteBtn.setDisable(true);
                    openBtn.setDisable(true);
                }
            }
        });

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteGroup();
            }
        });

        openBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = groupListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    Main.getInstance().setGroupIndex(index);
                    Main.getInstance().setPane(new GroupPane(), true, false);
                }
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

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
        });

        newBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewGroup();
            }
        });

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);

        busyPane.toFront();
    }

    public void setupList() {
        ObservableList<String> items = FXCollections.observableArrayList();

        for (TemplateGroup group : Main.getInstance().getProfile().groups) {
            items.add(group.groupName);
        }

        groupListView.setItems(items);
    }

    private void deleteGroup() {
        int index = groupListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Group");
            alert.setContentText("Are you sure you want to delete " + Main.getInstance().getProfile().groups.get(index).groupName + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                deleteIndex = index;
                groupListView.getItems().remove(index);
                groupListView.getSelectionModel().select(-1);
                deleteGroup = Main.getInstance().getProfile().groups.get(index);
                Main.getInstance().getProfile().groups.remove(index);
                showBusyWheel("Deleting Group...");
                isDelete = true;
                SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    private void createNewGroup() {
        showBusyWheel("Creating Group...");
        isNew = true;

        TemplateGroup group = new TemplateGroup();
        group.templates = new ArrayList<ReportTemplate>();

        int num = 1;
        String name = "Group 1";
        while (containsName(name)) {
            num++;
            name = "Group " + num;
        }
        group.groupName = name;

        ReportTemplate template = new ReportTemplate();
        template.name = "Report 1";
        template.course = "Course #";
        template.room = "RM #";
        template.origin = "Brigham City";
        template.facilitatorPresent = "Yes";
        template.techIssues = "None";
        template.cancellations = "None";
        template.assignmentsQuizzesExams = "None";
        template.facilitiesIssues = "None";
        template.comments = "None";
        template.instructor = "John Joe";
        template.time = "5:00pm - 7:45pm";
        group.templates.add(template);

        Main.getInstance().getProfile().groups.add(group);
        Main.getInstance().setGroupIndex(Main.getInstance().getProfile().groups.size() - 1);

        SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
    }

    private boolean containsName(String name) {
        for (TemplateGroup group : Main.getInstance().getProfile().groups) {
            if (group.groupName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResponse(BasicResponse response) {
        if (response.success) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (isNew) {
                        isNew = false;
                        hideBusyWheel();
                        Main.getInstance().setPane(new GroupPane(), true, false);
                    } else if (isDelete) {
                        isDelete = false;
                        hideBusyWheel();
                    }
                }
            });
        } else {
            if (isNew) {
                isNew = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.getInstance().getProfile().groups.remove(Main.getInstance().getGroupIndex());
                        setupList();
                        hideBusyWheel();
                        showAlert("Create Error", "An error occurred while creating group");
                    }
                });
            } else if (isDelete) {
                isDelete = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (deleteIndex >= Main.getInstance().getProfile().groups.size()) {
                            Main.getInstance().getProfile().groups.add(deleteGroup);
                        } else {
                            Main.getInstance().getProfile().groups.add(deleteIndex, deleteGroup);
                        }
                        setupList();
                        hideBusyWheel();
                        showAlert("Delete Error", "An error occurred while deleting group");
                    }
                });
            }
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showBusyWheel(String msg) {
        groupListView.getSelectionModel().select(-1);
        logoutBtn.setDisable(true);
        deleteBtn.setDisable(true);
        newBtn.setDisable(true);
        openBtn.setDisable(true);
        busyLbl.setText(msg);
        busyPane.setVisible(true);
    }

    private void hideBusyWheel() {
        groupListView.getSelectionModel().select(-1);
        logoutBtn.setDisable(false);
        deleteBtn.setDisable(true);
        newBtn.setDisable(false);
        openBtn.setDisable(true);
        busyPane.setVisible(false);
    }
}
