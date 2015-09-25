package app.panes;


import app.controllers.IBaseController;
import app.controllers.TemplateController;

/**
 * Created by jesse on 9/24/15.
 */
public class TemplatePane extends BasePane {

    @Override
    String getFXmlFileName() {
        return "template_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new TemplateController();
    }
}
