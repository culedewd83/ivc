package app.panes;

import app.controllers.IBaseController;
import app.controllers.TemplateListController;

/**
 * Created by jesse on 9/23/15.
 */
public class TemplateListPane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "template_list_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new TemplateListController();
    }

    @Override
    public void onPaneAppearing() {
        ((TemplateListController)mController).setupList();
    }
}
