package app.action;

import app.bean.UserService;
import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.ProtectedRoute;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import app.model.Student;
import app.model.User;
import app.utility.security.PasswordUtil;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Action(value = "account", label = "Account", roles = {"STUDENT"}, showLink = true)
@ProtectedRoute(roles = {"STUDENT"})
public class AccountAction {

    @Inject
    private UserService userService;

    @ActionGetMethod("list")
    public ActionResponse list(HttpSession session) {
        return profile(session);
    }

    @ActionGetMethod("profile")
    public ActionResponse profile(HttpSession session) {

        Student student = (Student) session.getAttribute("loggedInStudent");
        String successMessage = (String) session.getAttribute("successMessage");

        session.removeAttribute("successMessage");

        if (student == null) return new ActionResponse("<h2>Student not found</h2>");

        String html = """
<div class='account-page'>
    <div class='account-card'>

        %s

        <h2>My Account</h2>

        <div class='account-info'>
            <p><strong>Full Name:</strong> %s</p>
            <p><strong>Email:</strong> %s</p>
            <p><strong>Registration Number:</strong> %s</p>
            <p><strong>Username:</strong> %s</p>
        </div>

        <hr>

        <button class='toggle-btn' onclick='togglePasswordForm()'>
            Change Password
        </button>

        <div id='password-form' style='display:none;'>

            <form method='post' action='/Mentari/app/account/change-password'>
                <input type='password' name='password' placeholder='Enter new password' required>
                <button type='submit'>Update Password</button>
            </form>

        </div>

    </div>
</div>

<script>
function togglePasswordForm() {

    const form = document.getElementById('password-form');

    if (form.style.display === 'none') {
        form.style.display = 'block';
    } else {
        form.style.display = 'none';
    }
}
</script>

<style>
.account-page{ display:flex; justify-content:center; padding:40px; }
.account-card{ background:white; width:600px; padding:40px; border-radius:18px; box-shadow:0 4px 15px rgba(0,0,0,0.08); }
.account-card h2{ margin-bottom:30px; color:#071633; }
.account-info p{ margin-bottom:18px; font-size:16px; }
.account-card hr{ margin:30px 0; }
form{ display:flex; flex-direction:column; gap:15px; margin-top:20px; }
input{ padding:14px; border:1px solid #dcdcdc; border-radius:10px; }
button{ background:#2563eb; color:white; border:none; padding:14px; border-radius:10px; cursor:pointer; font-weight:600; }
button:hover{ background:#1d4ed8; }
.toggle-btn{ width:100%%; }
.success-message{
    background:#dcfce7;
    color:#166534;
    padding:14px;
    border-radius:10px;
    margin-bottom:20px;
    font-weight:600;
}
</style>
""".formatted(
                successMessage != null
                        ? "<div class='success-message'>" + successMessage + "</div>"
                        : "",
                student.getFullName(),
                student.getEmail(),
                student.getRegNumber(),
                student.getUser().getUsername()
        );

        return new ActionResponse(html);
    }

    @ActionPostMethod("change-password")
    public RedirectResponse changePassword(HttpServletRequest request, HttpSession session) {

        Student student = (Student) session.getAttribute("loggedInStudent");

        if (student == null) return new RedirectResponse("/Mentari/app/auth/login");

        String newPassword = request.getParameter("password");

        if (newPassword != null && !newPassword.isBlank()) {

            User user = student.getUser();

            user.setPassword(
                    PasswordUtil.hashPassword(
                            newPassword
                    )
            );

            userService.update(user);

            session.setAttribute("successMessage", "Password updated successfully");

            System.out.println("PASSWORD UPDATED FOR: " + user.getUsername());
        }

        return new RedirectResponse("/Mentari/app/account/list");
    }
}