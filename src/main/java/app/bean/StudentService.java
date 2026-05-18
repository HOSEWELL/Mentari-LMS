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

        if (
                student == null ||
                        !studentValidator.name(
                                student.getFullName()
                        )
        ) {

            return false;
        }

        /*
         * CREATE LOGIN ACCOUNT
         */
        User studentUser = new User();

        studentUser.setUsername(
                student.getEmail()
        );

        studentUser.setPassword(
                "@Mentari2026"
        );

        studentUser.setRole(
                "STUDENT"
        );

        User savedUser =
                userDao.save(studentUser);

        student.setUser(savedUser);

        /*
         * SAVE STUDENT PROFILE
         */
        studentDao.save(student);

        /*
         * FIRE CDI EVENT
         */
        studentEvent.fire(student);

        /*
         * AUDIT TRAIL
         */
        auditTrailBean.save(
                "Student enrolled: "
                        + student.getFullName()
                        + " ("
                        + student.getEmail()
                        + ")"
        );

        System.out.println(
                "MENTARI >>> Student enrolled: "
                        + student.getFullName()
        );

        return true;
    }

    /*
     * UPDATE STUDENT
     */
    public Student update(
            Student student
    ) {

        return studentDao.update(student);
    }

    /*
     * FIND ALL
     */
    public List<Student> findAll() {

        return studentDao.findAll();
    }
}