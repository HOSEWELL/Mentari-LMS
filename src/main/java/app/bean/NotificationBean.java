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

    // Fires automatically when StudentService fires the CDI event
    public void onStudentEnrolled(@Observes Student student) {
        sendWelcomeEmail(student);
        auditTrailBean.save("Welcome email sent to: " + student.getEmail());
    }

    private void sendWelcomeEmail(Student student) {
        // --- Email config ---
        final String senderEmail = "hosewellkaranja@gmail.com";
        final String senderPassword = "aayx iltm vqit gndr\n";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(student.getEmail()));
            message.setSubject("Welcome to Mentari LMS");
            message.setText(
                    "Dear " + student.getFullName() + ",\n\n" +
                            "You have been successfully enrolled in Mentari LMS.\n\n" +
                            "Your login credentials:\n" +
                            "  Username: " + student.getEmail() + "\n" +
                            "  Password: @Mentari2026\n\n" +
                            "Please log in and change your password immediately.\n\n" +
                            "Portal: http://localhost:8080/Mentari\n\n" +
                            "Regards,\nMentari LMS by Hosewell"
            );
            Transport.send(message);
            System.out.println("MENTARI >>> Welcome email sent to: " + student.getEmail());

        } catch (MessagingException e) {
            System.err.println("MENTARI >>> Email failed for: " + student.getEmail());
            e.printStackTrace();
        }
    }
}