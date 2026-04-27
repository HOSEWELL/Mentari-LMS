package app.action;

import app.framework.BaseAction;
import app.model.User;
import app.repository.JdbcRepository;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/student/account")
public class AccountAction extends BaseAction<User> {

    @Inject
    private JdbcRepository<User> userRepo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/student/account.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");

        User sessionUser = (User) req.getSession().getAttribute("loggedInUser");

        // 1. Verify current password
        if (sessionUser != null && sessionUser.getPassword().equals(currentPassword)) {

            // 2. Update password and save to DB
            sessionUser.setPassword(newPassword);
            userRepo.update(sessionUser);

            // 3. Clear session and force re-login with the new password
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?success=password_updated");
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/account?error=invalid_current_password");
        }
    }
}