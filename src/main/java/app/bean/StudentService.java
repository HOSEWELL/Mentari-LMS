package app.bean;

import app.dao.StudentDao;
import app.dao.UserDao;
import app.model.Student;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class StudentService {

    @Inject
    private StudentDao studentDao;

    @Inject
    private UserDao userDao;

    // CDI Event — fires NotificationBean.onStudentEnrolled()
    @Inject
    private Event<Student> studentEvent;

    @EJB
    private AuditTrailBean auditTrailBean;

    public void enrollStudent(Student student) {
        // 1. Create login account
        User studentUser = new User();
        studentUser.setUsername(student.getEmail());
        studentUser.setPassword("@Mentari2026");
        studentUser.setRole("STUDENT");

        User savedUser = userDao.save(studentUser);
        student.setUserId(savedUser.getId());

        // 2. Save student profile
        studentDao.save(student);

        // 3. Fire CDI event — NotificationBean will observe and send email
        studentEvent.fire(student);

        // 4. Log to audit trail
        auditTrailBean.save("Student enrolled: " + student.getFullName()
                + " (" + student.getEmail() + ")");

        System.out.println("MENTARI >>> Student enrolled: " + student.getFullName());
    }

    public List<Student> findAll() {
        return studentDao.findAll();
    }
}