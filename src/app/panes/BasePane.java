package app.panes;

import app.controllers.IBaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by Jesse Rogers on 8/12/2015.
 */
public abstract class BasePane extends Pane {

    protected IBaseController mController;

    public BasePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/" + getFXmlFileName()));
        mController = getController();
        fxmlLoader.setController(mController);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Pane root = fxmlLoader.getRoot();
        root.prefWidthProperty().bind(this.widthProperty());
        root.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(root);
        mController.init();
    }

    abstract String getFXmlFileName();
    abstract IBaseController getController();
    public void setupScene(Scene scene){}
    public void onPaneAppearing(){}
}
