package app.utils;

import app.models.ReportTemplate;
import app.models.TemplateGroup;

import java.util.ArrayList;

/**
 * Created by jesse on 9/23/15.
 */
public class Copy {

    public static TemplateGroup copyGroup(TemplateGroup group) {
        TemplateGroup result = new TemplateGroup();
        result.groupName = "" + group.groupName;
        result.templates = new ArrayList<ReportTemplate>();
        for (ReportTemplate template : group.templates) {
            ReportTemplate tCopy = new ReportTemplate();
            tCopy.assignmentsQuizzesExams = "" +template.assignmentsQuizzesExams;
            tCopy.cancellations = "" +template.cancellations;
            tCopy.comments = "" +template.comments;
            tCopy.course = "" +template.course;
            tCopy.facilitatorPresent = "" +template.facilitatorPresent;
            tCopy.facilitiesIssues = "" +template.facilitiesIssues;
            tCopy.origin = "" +template.origin;
            tCopy.room = "" +template.room;
            tCopy.techIssues = "" +template.techIssues;
            result.templates.add(tCopy);
        }
        return result;
    }

}
