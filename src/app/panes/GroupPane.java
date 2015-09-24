package app.panes;

import app.controllers.GroupController;
import app.controllers.IBaseController;

/**
 * Created by jesse on 9/22/15.
 */
public class GroupPane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "group_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new GroupController();
    }
}
