package app.bean;

import app.dao.StudentDao;
import app.dao.UserDao;
import app.model.Student;
import app.model.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class StudentService {

    @Inject
    private StudentDao studentDao;

    @Inject
    private UserDao userDao;

    public void enrollStudent(Student student) {
        // Business logic: create login account then link to student profile
        User studentUser = new User();
        studentUser.setUsername(student.getEmail());
        studentUser.setPassword("@Mentari2026");
        studentUser.setRole("STUDENT");

        User savedUser = userDao.save(studentUser);
        student.setUserId(savedUser.getId());
        studentDao.save(student);

        System.out.println("MENTARI >>> Student enrolled: " + student.getFullName());
    }

    public List<Student> findAll() {
        return studentDao.findAll();
    }
}