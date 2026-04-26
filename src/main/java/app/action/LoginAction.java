package app.action;

import app.model.User;
import app.repository.JdbcRepository;
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

    // Repository to access the users table
    private final JdbcRepository<User> userRepo = new JdbcRepository<>(User.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userParam = req.getParameter("username");
        String passParam = req.getParameter("password");

        // 1. Fetch all users (or ideally, create a findByUsername method later)
        List<User> allUsers = userRepo.findAll();
        User authenticatedUser = null;

        // 2. Search for a matching user in the list
        for (User u : allUsers) {
            if (u.getUsername().equals(userParam) && u.getPassword().equals(passParam)) {
                authenticatedUser = u;
                break;
            }
        }

        if (authenticatedUser != null) {
            HttpSession session = req.getSession(true);

            // Set session attributes for JSTL and Filters
            session.setAttribute("loggedInUser", authenticatedUser);
            session.setAttribute("username", authenticatedUser.getUsername());
            session.setAttribute("role", authenticatedUser.getRole());

            // 3. Redirect based on Role
            if ("ADMIN".equalsIgnoreCase(authenticatedUser.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin-dashboard");
            } else if ("STUDENT".equalsIgnoreCase(authenticatedUser.getRole())) {
                // Redirect to your student portal
                resp.sendRedirect(req.getContextPath() + "/student/portal");
            }
        } else {
            // 4. Failed login
            req.setAttribute("errorMessage", "Invalid Username or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}