package app;

import app.panes.BasePane;
import app.panes.StartPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private Stage mPrimaryStage;
    private static Main sInstance;

    public static Main getInstance() {
        return sInstance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        sInstance = this;
        mPrimaryStage = primaryStage;
        mPrimaryStage.setMinWidth(600);
        mPrimaryStage.setMinHeight(400);
        primaryStage.setTitle("IVC Reporting");
        setPane(new StartPane());
        primaryStage.show();
    }

    public void setPane(BasePane pane) {
        Scene scene = new Scene(pane, getStageWidth(), getStageHeight());
        pane.setupScene(scene);
        mPrimaryStage.setScene(scene);
        pane.onPaneAppearing();
    }

    public double getStageWidth() {
        if (mPrimaryStage.getScene() == null
                || Double.isNaN(mPrimaryStage.getScene().getWidth())
                || mPrimaryStage.getScene().getWidth() < 1.0) {
            return 600.0;
        }
        return mPrimaryStage.getScene().getWidth();
    }

    public double getStageHeight() {
        if (mPrimaryStage.getScene() == null
                || Double.isNaN(mPrimaryStage.getScene().getHeight())
                || mPrimaryStage.getScene().getHeight() < 1.0) {
            return 400.0;
        }
        return mPrimaryStage.getScene().getHeight();
    }
}
