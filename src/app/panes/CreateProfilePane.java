package app.panes;

import app.controllers.CreateProfileController;
import app.controllers.IBaseController;

/**
 * Created by jesse on 9/9/15.
 */
public class CreateProfilePane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "create_profile_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new CreateProfileController();
    }
}
