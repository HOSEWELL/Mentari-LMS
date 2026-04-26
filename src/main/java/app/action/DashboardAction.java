package app.action;

import app.listener.AppSessionListener;
import app.model.Student;
import app.model.Course; // Ensure you import your Course model
import app.repository.JdbcRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin-dashboard")
public class DashboardAction extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Get the session count from the listener
        int activeCount = AppSessionListener.getActiveSessionCount();
        req.setAttribute("activeSessions", activeCount);

        // 2. Fetch counts from the database
        try {
            // Initialize repositories
            JdbcRepository<Student> studentRepo = new JdbcRepository<>(Student.class);
            JdbcRepository<Course> courseRepo = new JdbcRepository<>(Course.class);

            // Fetch and count Students
            List<Student> students = studentRepo.findAll();
            int studentCount = (students != null) ? students.size() : 0;
            req.setAttribute("totalStudents", studentCount);

            // Fetch and count Courses (Replacing the hardcoded values)
            List<Course> courses = courseRepo.findAll();
            int courseCount = (courses != null) ? courses.size() : 0;

            // This attribute name should match ${activeCourses} in your dashboard.jsp
            req.setAttribute("activeCourses", courseCount);

        } catch (Exception e) {
            e.printStackTrace();
            // Default to 0 on failure to prevent the dashboard from breaking
            req.setAttribute("totalStudents", 0);
            req.setAttribute("activeCourses", 0);
        }

        // 3. Forward to the dashboard view
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}