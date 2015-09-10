package app.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by jesse on 9/9/15.
 */
public class StartController implements IBaseController {

    @FXML
    private Button loadBtn;

    @FXML
    private Button createBtn;

    @Override
    public void init() {
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadButtonClicked();
            }
        });

        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createButtonClicked();
            }
        });
    }

    private void loadButtonClicked() {

    }

    private void createButtonClicked() {

    }
}
