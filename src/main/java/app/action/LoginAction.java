package app.action;

import app.bean.UserService;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginAction extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Business logic now lives in the EJB
        User authenticatedUser = userService.authenticate(username, password);

        if (authenticatedUser != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedInUser", authenticatedUser);
            session.setAttribute("username", authenticatedUser.getUsername());
            session.setAttribute("role", authenticatedUser.getRole());

            if ("ADMIN".equalsIgnoreCase(authenticatedUser.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin-dashboard");
            } else if ("STUDENT".equalsIgnoreCase(authenticatedUser.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/student/portal");
            }
        } else {
            req.setAttribute("errorMessage", "Invalid Username or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}