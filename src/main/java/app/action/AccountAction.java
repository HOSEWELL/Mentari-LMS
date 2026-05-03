package app.action;

import app.bean.UserService;
import app.framework.BaseAction;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/student/account")
public class AccountAction extends BaseAction<User> {

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/student/account.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        User sessionUser = (User) req.getSession().getAttribute("loggedInUser");

        if (sessionUser != null && sessionUser.getPassword().equals(currentPassword)) {
            sessionUser.setPassword(newPassword);
            userService.update(sessionUser);
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?success=password_updated");
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/account?error=invalid_current_password");
        }
    }
}