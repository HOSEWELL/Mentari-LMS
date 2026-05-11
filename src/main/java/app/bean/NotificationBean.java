package app.bean;

import app.model.Student;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@Singleton
public class NotificationBean {

    @Resource(lookup = "java:jboss/mail/MentariMail")
    private Session mailSession;

    @EJB
    private AuditTrailBean auditTrailBean;

    public void sendAdminEmail(String subject, String content) {
        sendEmail("hosewellkaranja@gmail.com", subject, content);
        auditTrailBean.save("Admin Notification Sent: " + subject);
    }

    public void onStudentEnrolled(@Observes Student student) {
        String subject = "Welcome to Mentari LMS";
        String body = "Dear " + student.getFullName() + ",\n\n"
                + "You have been successfully enrolled in Mentari LMS.\n\n"
                + "Your login credentials:\n"
                + "  Username: " + student.getEmail() + "\n"
                + "  Password: @Mentari2026\n\n"
                + "Please log in and change your password immediately.\n\n"
                + "Portal: http://localhost:8080/Mentari\n\n"
                + "Regards,\nMentari LMS by Hosewell";

        sendEmail(student.getEmail(), subject, body);
        auditTrailBean.save("Welcome email sent to: " + student.getEmail());
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("hosewellkaranja@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("MENTARI >>> Email sent to: " + recipient);

        } catch (MessagingException e) {
            System.err.println("MENTARI >>> Email failed to: " + recipient);
            e.printStackTrace();
            auditTrailBean.save("CRITICAL: Email delivery failed to " + recipient);
        }
    }
}