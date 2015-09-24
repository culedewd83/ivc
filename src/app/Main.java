package app;

import app.models.Profile;
import app.panes.BasePane;
import app.panes.StartPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;


public class Main extends Application {

    private Stage mPrimaryStage;
    private static Main sInstance;
    private Stack<Scene> mStack;
    private Profile mProfile;
    private int mGroupIndex;

    public static Main getInstance() {
        return sInstance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        sInstance = this;
        mStack = new Stack<Scene>();
        mPrimaryStage = primaryStage;
        mPrimaryStage.setMinWidth(600);
        mPrimaryStage.setMinHeight(400);
        primaryStage.setTitle("IVC Reporting");
        setPane(new StartPane(), true, false);
        primaryStage.show();
    }

    public void setPane(BasePane pane, boolean addToStack, boolean clearStack) {
        if (clearStack) {
            mStack = new Stack<Scene>();
        }

        Scene scene = new Scene(pane, getStageWidth(), getStageHeight());
        pane.setupScene(scene);
        if (addToStack) {
            mStack.push(scene);
        }
        mPrimaryStage.setScene(scene);
        pane.onPaneAppearing();
    }

    public void popPaneStack() {
        if (mStack.size() < 2) {
            return;
        }
        mStack.pop();
        mPrimaryStage.setScene(mStack.peek());
        ((BasePane)mStack.peek().getRoot()).onPaneAppearing();
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

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

    public Profile getProfile() {
        return mProfile;
    }

    public void setGroupIndex(int index) {
        mGroupIndex = index;
    }

    public int getGroupIndex() {
        return mGroupIndex;
    }
}