package app.bean;

import app.dao.AuditTrailDao;
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

import java.security.SecureRandom;
import java.util.List;

@Stateless
public class StudentService {

    @Inject
    private StudentDao studentDao;

    @Inject
    private UserDao userDao;

    @Inject
    private AuditTrailDao auditTrailDao;

    @Inject
    @Named("Student")
    private Validate<String> studentValidator;

    @Inject
    private Event<Student> studentEvent;

    @EJB
    private AuditTrailBean auditTrailBean;

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";

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
         * GENERATE RANDOM PASSWORD
         */
        String generatedPassword =
                generateRandomPassword(10);

        /*
         * CREATE LOGIN ACCOUNT
         */
        User studentUser = new User();

        studentUser.setUsername(
                student.getEmail()
        );

        studentUser.setPassword(
                generatedPassword
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
     * GENERATE PASSWORD
     */
    private String generateRandomPassword(
            int length
    ) {

        SecureRandom random =
                new SecureRandom();

        StringBuilder password =
                new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(
                    CHARACTERS.length()
            );

            password.append(
                    CHARACTERS.charAt(index)
            );
        }

        return password.toString();
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