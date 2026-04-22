package app.action;

import app.listener.AppSessionListener;
import app.model.Student; // Import your student model
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

        // 1. Get the session count from your listener
        int activeCount = AppSessionListener.getActiveSessionCount();
        req.setAttribute("activeSessions", activeCount);

        // 2. Fetch the student count from the database
        try {
            // Initialize the repository for the Student class
            JdbcRepository<Student> studentRepo = new JdbcRepository<>(Student.class);

            // Fetch all students to get the size
            List<Student> students = studentRepo.findAll();
            int studentCount = (students != null) ? students.size() : 0;

            // This attribute name MUST match the one in your dashboard.jsp ${totalStudents}
            req.setAttribute("totalStudents", studentCount);

        } catch (Exception e) {
            // If database fails, default to 0 so the page doesn't crash
            e.printStackTrace();
            req.setAttribute("totalStudents", 0);
        }

        // 3. Forward to the dashboard view
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}