package app.panes;

import app.controllers.IBaseController;
import app.controllers.StartController;

/**
 * Created by jesse on 9/9/15.
 */
public class StartPane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "start_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new StartController();
    }
}
