package app.utils;

import app.models.Profile;
import app.models.ReportTemplate;
import app.models.TemplateGroup;

/**
 * Created by jesse on 9/28/15.
 */
public class ProfileUtils {

    public static Profile sanatize(Profile profile) {
        for (TemplateGroup group : profile.groups) {
            if (nullOrWhitespace(group.groupName)) group.groupName = "Group";
            for (ReportTemplate template : group.templates) {
                if (nullOrWhitespace(template.name)) template.name = "Report 1";
                if (nullOrWhitespace(template.course)) template.course = "Course #";
                if (nullOrWhitespace(template.room)) template.room = "RM #";
                if (nullOrWhitespace(template.origin)) template.origin = "Brigham City";
                if (nullOrWhitespace(template.facilitatorPresent)) template.facilitatorPresent = "Yes";
                if (nullOrWhitespace(template.techIssues)) template.techIssues = "None";
                if (nullOrWhitespace(template.cancellations)) template.cancellations = "None";
                if (nullOrWhitespace(template.assignmentsQuizzesExams)) template.assignmentsQuizzesExams = "None";
                if (nullOrWhitespace(template.facilitiesIssues)) template.facilitiesIssues = "None";
                if (nullOrWhitespace(template.comments)) template.comments = "None";
                if (nullOrWhitespace(template.instructor)) template.instructor = "John Joe";
                if (nullOrWhitespace(template.time)) template.time = "5:00pm - 7:45pm";
            }
        }
        return profile;
    }

    public static boolean nullOrWhitespace(String str) {
        return (str == null || str.trim().length() == 0);
    }
}
