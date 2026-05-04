package app.bean;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import java.util.Date;

@Singleton
public class EmailReminderBean {

    @EJB
    private AuditTrailBean auditTrailBean;

    // Fires every day at 8:00 AM
    @Schedule(second = "0", minute = "0", hour = "8", persistent = false)
    public void dailySystemCheck() {
        String msg = "Daily system check completed at: " + new Date();
        System.out.println("MENTARI >>> " + msg);
        auditTrailBean.save(msg);
    }

    // Fires every Monday at 9:00 AM — reminds admin to review pending requests
    @Schedule(second = "0", minute = "0", hour = "9", dayOfWeek = "Mon", persistent = false)
    public void weeklyAdminReminder() {
        String msg = "Weekly reminder: Admin should review pending deferral requests.";
        System.out.println("MENTARI >>> " + msg);
        auditTrailBean.save(msg);
    }
}