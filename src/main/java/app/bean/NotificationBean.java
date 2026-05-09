package app.bean;

import app.model.Student;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

@Singleton
public class NotificationBean {

    @EJB
    private AuditTrailBean auditTrailBean;

    // Configuration - Using your credentials from the Mentari project
    private final String adminEmail = "hosewellkaranja@gmail.com";
    private final String senderEmail = "hosewellkaranja@gmail.com";
    private final String senderPassword = "aayx iltm vqit gndr";

    /**
     * Generic method to send emails to the Admin.
     * Logs the action to the Audit Trail interface automatically.
     */
    public void sendAdminEmail(String subject, String content) {
        sendEmail(adminEmail, subject, content);
        auditTrailBean.save("Admin Notification Sent: " + subject);
    }

    /**
     * CDI Observer: Fires automatically when a Student is enrolled.
     */
    public void onStudentEnrolled(@Observes Student student) {
        String subject = "Welcome to Mentari LMS";
        String body = "Dear " + student.getFullName() + ",\n\n" +
                "You have been successfully enrolled in Mentari LMS.\n\n" +
                "Your login credentials:\n" +
                "  Username: " + student.getEmail() + "\n" +
                "  Password: @Mentari2026\n\n" +
                "Please log in and change your password immediately.\n\n" +
                "Portal: http://localhost:8080/Mentari\n\n" +
                "Regards,\nMentari LMS by Hosewell";

        sendEmail(student.getEmail(), subject, body);
        auditTrailBean.save("Welcome email sent to student: " + student.getEmail());
    }

    /**
     * Private helper to handle the heavy lifting of Jakarta Mail / SMTP logic.
     */
    private void sendEmail(String recipient, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("MENTARI >>> Email successfully sent to: " + recipient);

        } catch (MessagingException e) {
            System.err.println("MENTARI >>> Failed to send email to: " + recipient);
            e.printStackTrace();

            auditTrailBean.save("CRITICAL: Email delivery failed to " + recipient);
        }
    }
}