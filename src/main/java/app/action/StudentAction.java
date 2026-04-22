package app.action;

import app.model.Student;
import app.repository.JdbcRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/students")
public class StudentAction extends HttpServlet {

    private final JdbcRepository<Student> studentRepo = new JdbcRepository<>(Student.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Fetch all students from DB
        List<Student> students = studentRepo.findAll();
        req.setAttribute("students", students);

        // 2. Forward to the JSP
        req.getRequestDispatcher("/WEB-INF/views/admin/students.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. Capture form data
            String name = req.getParameter("fullName");
            String email = req.getParameter("email");
            String regNum = req.getParameter("regNumber");

            // 2. Create and Save Student object
            Student newStudent = new Student();
            newStudent.setFullName(name);
            newStudent.setEmail(email);
            newStudent.setRegNumber(regNum);

            studentRepo.save(newStudent);

            // 3. Redirect back to the list with a success message
            resp.sendRedirect(req.getContextPath() + "/admin/students?success=true");
        } catch (Exception e) {
            e.printStackTrace(); // This will show the specific error in your console
            resp.sendRedirect(req.getContextPath() + "/admin/students?error=true");
        }
    }
}