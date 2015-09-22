package app.panes;

import app.controllers.GroupListController;
import app.controllers.IBaseController;

/**
 * Created by jesse on 9/21/15.
 */
public class GroupListPane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "group_list_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new GroupListController();
    }
}
