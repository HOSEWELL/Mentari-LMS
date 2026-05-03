package app.action;

import app.bean.CourseService;
import app.bean.StudentService;
import app.listener.AppSessionListener;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin-dashboard")
public class DashboardAction extends HttpServlet {

    @EJB
    private StudentService studentService;

    @EJB
    private CourseService courseService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Expose loggedInUser to request scope for dashboard.jsp header
        req.setAttribute("loggedInUser", req.getSession().getAttribute("loggedInUser"));

        req.setAttribute("activeSessions", AppSessionListener.getActiveSessionCount());
        req.setAttribute("totalStudents", studentService.findAll().size());
        req.setAttribute("activeCourses", courseService.findAll().size());
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}