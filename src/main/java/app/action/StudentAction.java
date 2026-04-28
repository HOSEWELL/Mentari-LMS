package app.action;

import app.framework.BaseAction;
import app.model.Student;
import app.model.User;
import app.repository.JdbcRepository;
import app.util.validation.Validate;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/students")
public class StudentAction extends BaseAction<Student> {

    private Validate<String> studentValidator;
    private JdbcRepository<Student> studentRepo;
    private JdbcRepository<User> userRepo;

    public StudentAction() {
        super();
    }

    // ---SETTER INJECTION FOR VALIDATOR ---
    @Inject
    public void setStudentValidator(@Named("Student") Validate<String> studentValidator) {
        this.studentValidator = studentValidator;
    }

    // --- TASK: SETTER INJECTION FOR REPOSITORIES ---
    @Inject
    public void setStudentRepo(JdbcRepository<Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Inject
    public void setUserRepo(JdbcRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = studentRepo.findAll();
        req.setAttribute("students", students);
        req.getRequestDispatcher("/WEB-INF/views/admin/students.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Serialize and Validate the form
        Student student = super.serializeForm(req.getParameterMap(), studentValidator);

        if (student != null) {
            // 2. Create User Account with temporary password
            User studentUser = new User();
            studentUser.setUsername(student.getEmail());
            studentUser.setPassword("@Mentari2026");
            studentUser.setRole("STUDENT");

            // 3. Save User and get the generated ID back from the DB
            User savedUser = userRepo.save(studentUser);

            // 4. Link Student to the User via the generated ID
            student.setUserId(savedUser.getId());

            // 5. Save the Student profile
            studentRepo.save(student);

            System.out.println("MENTARI >>> Student profile and Login account created successfully.");
            resp.sendRedirect(req.getContextPath() + "/admin/students?success=true");
        } else {
            // Validation failed (e.g., name was "Mike")
            resp.sendRedirect(req.getContextPath() + "/admin/students?error=blocked_name");
        }
    }
}