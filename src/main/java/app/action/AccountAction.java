package app.action;

import app.bean.StudentService;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.ProtectedRoute;

import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;

import app.model.Student;
import app.model.User;

import jakarta.inject.Inject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Action(
        value = "account",
        label = "Account",
        roles = {"STUDENT"},
        showLink = true
)
@ProtectedRoute(
        roles = {"STUDENT"}
)
public class AccountAction {

    @Inject
    private StudentService studentService;

    /*
     * =========================
     * ACCOUNT PAGE
     * =========================
     */
    @ActionGetMethod("list")
    public ActionResponse list(
            HttpSession session
    ) {

        return profile(session);
    }

    /*
     * =========================
     * PROFILE PAGE
     * =========================
     */
    @ActionGetMethod("profile")
    public ActionResponse profile(
            HttpSession session
    ) {

        Student student =
                (Student) session.getAttribute(
                        "loggedInStudent"
                );

        if (student == null) {

            return new ActionResponse(
                    "<h2>Student not found</h2>"
            );
        }

        String html = """

<div class='account-page'>

    <div class='account-card'>

        <h2>My Account</h2>

        <div class='account-info'>

            <p>
                <strong>Full Name:</strong>
                %s
            </p>

            <p>
                <strong>Email:</strong>
                %s
            </p>

            <p>
                <strong>Registration Number:</strong>
                %s
            </p>

            <p>
                <strong>Username:</strong>
                %s
            </p>

        </div>

        <hr>

        <h3>Change Password</h3>

        <form method='post'
              action='/Mentari/app/account/change-password'>

            <input
                    type='password'
                    name='password'
                    placeholder='Enter new password'
                    required
            >

            <button type='submit'>
                Update Password
            </button>

        </form>

    </div>

</div>

<style>

.account-page{
    display:flex;
    justify-content:center;
    padding:40px;
}

.account-card{
    background:white;
    width:600px;
    padding:40px;
    border-radius:18px;
    box-shadow:0 4px 15px rgba(0,0,0,0.08);
}

.account-card h2{
    margin-bottom:30px;
    color:#071633;
}

.account-info p{
    margin-bottom:18px;
    font-size:16px;
}

.account-card hr{
    margin:30px 0;
}

form{
    display:flex;
    flex-direction:column;
    gap:15px;
}

input{
    padding:14px;
    border:1px solid #dcdcdc;
    border-radius:10px;
}

button{
    background:#2563eb;
    color:white;
    border:none;
    padding:14px;
    border-radius:10px;
    cursor:pointer;
    font-weight:600;
}

button:hover{
    background:#1d4ed8;
}

</style>

""".formatted(
                student.getFullName(),
                student.getEmail(),
                student.getRegNumber(),
                student.getUser().getUsername()
        );

        return new ActionResponse(html);
    }

    /*
     * =========================
     * CHANGE PASSWORD
     * =========================
     */
    @ActionPostMethod("change-password")
    public RedirectResponse changePassword(
            HttpServletRequest request,
            HttpSession session
    ) {

        Student student =
                (Student) session.getAttribute(
                        "loggedInStudent"
                );

        if (student != null) {

            String newPassword =
                    request.getParameter(
                            "password"
                    );

            User user =
                    student.getUser();

            if (
                    newPassword != null
                            &&
                            !newPassword.isBlank()
            ) {

                user.setPassword(
                        newPassword
                );

                studentService.update(
                        student
                );
            }
        }

        return new RedirectResponse(
                "/Mentari/app/account/list"
        );
    }
}