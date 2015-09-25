package app.controllers;

import app.Main;
import app.models.ReportTemplate;
import app.panes.StartPane;
import app.panes.TemplatePane;
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

import java.util.Optional;

/**
 * Created by jesse on 9/23/15.
 */
public class TemplateListController implements IBaseController, IResponse {

    @FXML
    Label greetingLbl;

    @FXML
    Button logoutBtn;

    @FXML
    ListView groupListView;

    @FXML
    Button backBtn;

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
    ReportTemplate deleteTemplate;
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

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.getInstance().popPaneStack();
            }
        });

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteTemplate();
            }
        });

        openBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = groupListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    Main.getInstance().setTemplateIndex(index);
                    Main.getInstance().setPane(new TemplatePane(), true, false);
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
                    Main.getInstance().setTemplateIndex(-1);
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
                createNewTemplate();
            }
        });

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);

        busyPane.toFront();
    }

    public void setupList() {
        ObservableList<String> items = FXCollections.observableArrayList();

        for (ReportTemplate template : Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates) {
            items.add(template.name);
        }

        groupListView.setItems(items);
    }

    private void deleteTemplate() {
        int groupIndex = Main.getInstance().getGroupIndex();
        int templateIndex = groupListView.getSelectionModel().getSelectedIndex();
        if (templateIndex >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Report Template");
            alert.setContentText("Are you sure you want to delete "
                    + Main.getInstance().getProfile().groups.get(groupIndex).templates.get(templateIndex).name + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                deleteIndex = templateIndex;
                groupListView.getItems().remove(templateIndex);
                groupListView.getSelectionModel().select(-1);
                deleteTemplate = Main.getInstance().getProfile().groups.get(groupIndex).templates.get(templateIndex);
                Main.getInstance().getProfile().groups.get(groupIndex).templates.remove(templateIndex);
                showBusyWheel("Deleting Group...");
                isDelete = true;
                SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    private void createNewTemplate() {
        showBusyWheel("Creating Template...");
        isNew = true;

        ReportTemplate template = new ReportTemplate();

        int num = 1;
        String name = "Report 1";
        while (containsName(name)) {
            num++;
            name = "Report " + num;
        }
        template.name = name;
        template.course = "Course #";
        template.room = "RM #";
        template.origin = "Brigham City";
        template.facilitatorPresent = "Yes";
        template.techIssues = "None";
        template.cancellations = "None";
        template.assignmentsQuizzesExams = "None";
        template.facilitiesIssues = "None";
        template.comments = "None";

        Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates.add(template);
        Main.getInstance().setTemplateIndex(Main.getInstance()
                .getProfile()
                .groups
                .get(Main.getInstance().getGroupIndex())
                .templates.size() - 1);
        deleteIndex = Main.getInstance().getTemplateIndex();

        SaveProfileRequest.saveProfile(Main.getInstance().getProfile(), this);
    }

    private boolean containsName(String name) {
        for (ReportTemplate template : Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates) {
            if (template.name.equals(name)) {
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
                        Main.getInstance().setPane(new TemplatePane(), true, false);
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
                        Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates.remove(deleteIndex);
                        setupList();
                        hideBusyWheel();
                        showAlert("Create Error", "An error occurred while creating report template");
                    }
                });
            } else if (isDelete) {
                isDelete = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (deleteIndex >= Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates.size()) {
                            Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates.add(deleteTemplate);
                        } else {
                            Main.getInstance().getProfile().groups.get(Main.getInstance().getGroupIndex()).templates.add(deleteIndex, deleteTemplate);
                        }
                        setupList();
                        hideBusyWheel();
                        showAlert("Delete Error", "An error occurred while deleting report template");
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
