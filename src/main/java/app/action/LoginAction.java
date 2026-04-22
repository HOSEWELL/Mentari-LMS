package app.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginAction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userParam = req.getParameter("username");
        String passParam = req.getParameter("password");

        // Hardcoded Admin Credentials check
        if ("admin".equals(userParam) && "12345".equals(passParam)) {
            HttpSession session = req.getSession(true);

            // Using "loggedInUser" consistently to match your sidebar JSTL
            // We create a simple object or just set attributes
            session.setAttribute("username", "Admin");
            session.setAttribute("role", "ADMIN");

            // To make ${sessionScope.loggedInUser.role} work in JSP:
            app.model.User adminUser = new app.model.User();
            adminUser.setUsername("Admin");
            adminUser.setRole("ADMIN");
            session.setAttribute("loggedInUser", adminUser);

            // Redirect to ADMIN path
            resp.sendRedirect(req.getContextPath() + "/admin-dashboard");

        } else {
            // Logic for Student login would go here (fetching from DB)
            // If both fail:
            req.setAttribute("errorMessage", "Invalid Username or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}