package app.action;

import app.bean.StudentService;
import app.bean.UserService;
import app.model.Student;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginAction extends HttpServlet {

    @EJB
    private UserService userService;

    // Inject StudentService
    @EJB
    private StudentService studentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Authenticate user using EJB
        User authenticatedUser = userService.authenticate(username, password);

        if (authenticatedUser != null) {
            HttpSession session = req.getSession(true);

            // Store common session attributes
            session.setAttribute("loggedInUser", authenticatedUser);
            session.setAttribute("username", authenticatedUser.getUsername());
            session.setAttribute("role", authenticatedUser.getRole());

            if ("ADMIN".equalsIgnoreCase(authenticatedUser.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin-dashboard");

            } else if ("STUDENT".equalsIgnoreCase(authenticatedUser.getRole())) {

                // Find the student profile linked to this user
                List<Student> allStudents = studentService.findAll();

                for (Student s : allStudents) {
                    if (s.getUserId() != null
                            && s.getUserId().equals(authenticatedUser.getId())) {
                        session.setAttribute("loggedInStudent", s);
                        break;
                    }
                }

                resp.sendRedirect(req.getContextPath() + "/student/portal");

            } else {
                // Optional fallback for other roles
                resp.sendRedirect(req.getContextPath() + "/");
            }

        } else {
            req.setAttribute("errorMessage", "Invalid Username or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}