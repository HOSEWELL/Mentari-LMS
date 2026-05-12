package app.bean;

import app.dao.StudentDao;
import app.dao.UserDao;
import app.model.Student;
import app.model.User;
import app.utility.validation.Validate;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Stateless
public class StudentService {

    @Inject
    private StudentDao studentDao;

    @Inject
    private UserDao userDao;

    @Inject
    @Named("Student")
    private Validate<String> studentValidator;

    @Inject
    private Event<Student> studentEvent;

    @EJB
    private AuditTrailBean auditTrailBean;

    public boolean enrollStudent(Student student) {
        if (student == null ||
                !studentValidator.name(student.getFullName())) {
            return false;
        }

        // Create login account
        User studentUser = new User();
        studentUser.setUsername(student.getEmail());
        studentUser.setPassword("@Mentari2026");
        studentUser.setRole("STUDENT");

        User savedUser = userDao.save(studentUser);
        student.setUserId(savedUser.getId());

        // Save student profile
        studentDao.save(student);

        // Fire CDI event
        studentEvent.fire(student);

        // Audit trail
        auditTrailBean.save(
                "Student enrolled: " + student.getFullName()
                        + " (" + student.getEmail() + ")"
        );

        System.out.println(
                "MENTARI >>> Student enrolled: " + student.getFullName()
        );

        return true;
    }

    public List<Student> findAll() {
        return studentDao.findAll();
    }
}