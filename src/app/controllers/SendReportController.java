package app.controllers;

import app.Main;
import app.models.ReportTemplate;
import app.panes.StartPane;
import app.utils.Copy;
import app.utils.PasswordDialog;
import email.Email;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by jesse on 9/24/15.
 */
public class SendReportController implements IBaseController {

    @FXML
    AnchorPane busyPane;

    @FXML
    Label greetingLbl;

    @FXML
    Button logoutBtn;

    @FXML
    Button backBtn;

    @FXML
    Button nextBtn;

    @FXML
    Button cancelBtn;

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

    @FXML
    TextField instructor;

    @FXML
    TextField time;

    private int mGroupIndex;
    private int mTemplateIndex = 0;
    private List<ReportTemplate> mTemplates;

    @Override
    public void init() {

        mGroupIndex = Main.getInstance().getGroupIndex();

        gridPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(18.0));

        greetingLbl.setText("Hello, " + Main.getInstance().getProfile().name);

        mTemplates = Copy.copyGroup(Main.getInstance()
                .getProfile()
                .groups
                .get(mGroupIndex)).templates;

        fillTextFields();

        if (mTemplateIndex == mTemplates.size() - 1) {
            nextBtn.setText("Send");
        }

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logoutBtnClicked();
            }
        });

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelBtnClicked();
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backBtnClicked();
            }
        });

        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextBtnClicked();
            }
        });

        busyPane.toFront();
    }

    private void fillTextFields() {
        assignments.setText(mTemplates.get(mTemplateIndex).assignmentsQuizzesExams);
        cancellations.setText(mTemplates.get(mTemplateIndex).cancellations);
        comments.setText(mTemplates.get(mTemplateIndex).comments);
        className.setText(mTemplates.get(mTemplateIndex).course);
        facilitatorPresent.setText(mTemplates.get(mTemplateIndex).facilitatorPresent);
        facilitiesIssues.setText(mTemplates.get(mTemplateIndex).facilitiesIssues);
        name.setText(mTemplates.get(mTemplateIndex).name);
        origin.setText(mTemplates.get(mTemplateIndex).origin);
        room.setText(mTemplates.get(mTemplateIndex).room);
        techIssues.setText(mTemplates.get(mTemplateIndex).techIssues);
        instructor.setText(mTemplates.get(mTemplateIndex).instructor);
        time.setText(mTemplates.get(mTemplateIndex).time);
    }

    private void logoutBtnClicked() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure you want to logout before sending the report?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main.getInstance().setGroupIndex(-1);
            Main.getInstance().setProfile(null);
            Main.getInstance().setPane(new StartPane(), true, true);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void cancelBtnClicked () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancel");
        alert.setContentText("Are you sure you want cancel sending the report?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main.getInstance().popPaneStack();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void backBtnClicked() {
        trimAll();
        mTemplates.set(mTemplateIndex, getTemplate());

        mTemplateIndex--;
        mTemplateIndex = mTemplateIndex < 0 ? 0 : mTemplateIndex;
        if (mTemplateIndex == 0) {
            backBtn.setDisable(true);
        }

        nextBtn.setText("Next");

        scrollPane.setVvalue(0);
        fillTextFields();
    }

    private void nextBtnClicked() {
        trimAll();
        mTemplates.set(mTemplateIndex, getTemplate());

        if (mTemplateIndex == mTemplates.size() - 1) {
            sendReport();
            return;
        }

        backBtn.setDisable(false);

        mTemplateIndex++;
        if (mTemplateIndex == mTemplates.size() - 1) {
            nextBtn.setText("Send");
        }

        scrollPane.setVvalue(0);
        fillTextFields();
    }

    private class Haha extends TextInputDialog {


    }

    private void sendReport() {
        PasswordDialog dialog = new PasswordDialog();
        dialog.setTitle("Send Report");
        dialog.setHeaderText("Please enter your USU password");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String password = result.get();
            String emailBody = getEmailBody();

            showBusyWheel();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean didSend = Email.sendEmail(emailBody, password);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            hideBusyWheel();
                            if (didSend) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Report");
                                alert.setHeaderText("Report Sent");
                                alert.setContentText("The report was sent successfully");
                                alert.showAndWait();
                                Main.getInstance().popPaneStack();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error Sending Report");
                                alert.setContentText("An error occurred sending the report. Please check your password and try again.");
                                alert.showAndWait();
                            }
                        }
                    });
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    private String getEmailBody() {
        StringBuilder sb = new StringBuilder();

        sb.append("Facilitator: ").append(Main.getInstance().getProfile().name).append("\n");
        sb.append("Date: ").append(new SimpleDateFormat("MM/dd/yyyy").format(new Date())).append("\n\n") ;

        for (ReportTemplate report : mTemplates) {
            sb.append("Class: ").append(report.course).append("\n");
            sb.append("Room #: ").append(report.room).append("\n");
            sb.append("Instructor: ").append(report.instructor).append("\n");
            sb.append("Meeting Time: ").append(report.time).append("\n");
            sb.append("Originates at: ").append(report.origin).append("\n");
            sb.append("Facilitator Present: ").append(report.facilitatorPresent).append("\n");
            sb.append("Tech Issues: ").append(report.techIssues).append("\n");
            sb.append("Cancellations: ").append(report.cancellations).append("\n");
            sb.append("Assignments/Quizzes/Exams: ").append(report.assignmentsQuizzesExams).append("\n");
            sb.append("Facilities Issues: ").append(report.facilitiesIssues).append("\n\n");
            sb.append("Additional Comments: ").append(report.comments).append("\n\n\n");
        }

        sb.append("This report was generated by the IVC Reporting Tool ").append(Main.VERSION).append("\n");
        sb.append("IVC Reporting Tool was created by Jesse Rogers\n");

        return sb.toString();
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
        instructor.setText(instructor.getText().trim());
        time.setText(time.getText().trim());
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
        result.instructor = instructor.getText();
        result.time = time.getText();
        return result;
    }

    private void showBusyWheel() {
        logoutBtn.setDisable(true);
        cancelBtn.setDisable(true);
        backBtn.setDisable(true);
        nextBtn.setDisable(true);

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
        instructor.setEditable(false);
        time.setEditable(false);

        busyPane.setVisible(true);
    }

    private void hideBusyWheel() {
        logoutBtn.setDisable(false);
        cancelBtn.setDisable(false);
        backBtn.setDisable(mTemplateIndex != 0);
        nextBtn.setDisable(false);

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
        instructor.setEditable(true);
        time.setEditable(true);

        busyPane.setVisible(false);
    }
}
