package app.bean;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import java.util.Date;

@Singleton
public class EmailReminderBean {

    @EJB
    private NotificationBean notificationBean;

    @Schedule(second = "0", minute = "0", hour = "8", persistent = false)
    public void dailySystemCheck() {
        String subject = "Mentari System Health Check";
        String body = "System is running normally as of " + new Date();

        // This sends the email AND logs to Audit Trail via NotificationBean
        notificationBean.sendAdminEmail(subject, body);
    }

    @Schedule(second = "0", minute = "0", hour = "9", dayOfWeek = "Mon", persistent = false)
    public void weeklyAdminReminder() {
        String subject = "LMS Action Required: Pending Deferrals";
        String body = "Hello Hosewell,\n\nPlease review the pending student deferral requests for this week.";

        notificationBean.sendAdminEmail(subject, body);
    }
}