package app.action;

import app.listener.AppSessionListener;
import app.model.Student;
import app.model.Course;
import app.repository.JdbcRepository;
import jakarta.inject.Inject; // Added
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin-dashboard")
public class DashboardAction extends HttpServlet {

    // --- FIELD INJECTION ---
    @Inject
    private JdbcRepository<Student> studentRepo;

    @Inject
    private JdbcRepository<Course> courseRepo;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Get the session count from the listener
        int activeCount = AppSessionListener.getActiveSessionCount();
        req.setAttribute("activeSessions", activeCount);

        // 2. Fetch counts from the database
        try {
            // Fetch and count Students
            List<Student> students = studentRepo.findAll();
            int studentCount = (students != null) ? students.size() : 0;
            req.setAttribute("totalStudents", studentCount);

            // Fetch and count Courses
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