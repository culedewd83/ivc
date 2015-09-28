package app.panes;

import app.controllers.IBaseController;
import app.controllers.SendReportController;

/**
 * Created by jesse on 9/24/15.
 */
public class SendReportPane extends BasePane {
    @Override
    String getFXmlFileName() {
        return "send_report_pane.fxml";
    }

    @Override
    IBaseController getController() {
        return new SendReportController();
    }
}
