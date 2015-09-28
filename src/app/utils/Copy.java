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
            result.templates.add(copyTemplate(template));
        }
        return result;
    }

    public static ReportTemplate copyTemplate(ReportTemplate template) {
        ReportTemplate result = new ReportTemplate();
        result.assignmentsQuizzesExams = "" + template.assignmentsQuizzesExams;
        result.cancellations = "" + template.cancellations;
        result.comments = "" + template.comments;
        result.course = "" + template.course;
        result.facilitatorPresent = "" + template.facilitatorPresent;
        result.facilitiesIssues = "" + template.facilitiesIssues;
        result.name = "" + template.name;
        result.origin = "" + template.origin;
        result.room = "" + template.room;
        result.techIssues = "" + template.techIssues;
        result.instructor = "" + template.instructor;
        result.time = "" + template.time;
        return result;
    }

}
