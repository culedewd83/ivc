package app.panes;

import app.controllers.IBaseController;
import app.controllers.LoadProfileController;

/**
 * Created by jesse on 9/21/15.
 */
public class LoadProfilePane extends BasePane {

    public LoadProfilePane() {
        super();
    }

    public LoadProfilePane(String key) {
        super(key);
    }

    @Override
    String getFXmlFileName() {
        return "load_profile_pane.fxml";
    }

    @Override
    IBaseController getController() {
        if (mKey != null && mKey.length() > 0) {
            return new LoadProfileController(mKey);
        }

        return new LoadProfileController();
    }
}
