package app.action;

import app.bean.StudentService;
import app.bean.UserService;

import app.framework.annotation.*;
import app.framework.binding.BaseAction;
import app.framework.rendering.AuthPageRenderer;

import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;

import app.model.Student;
import app.model.User;

import jakarta.inject.Inject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@PublicRoute

@Action(
        value = "auth",
        showLink = false
)
public class AuthAction extends BaseAction {

    @Inject
    private UserService userService;

    @Inject
    private StudentService studentService;

    @Inject
    private AuthPageRenderer authPageRenderer;

    /*
     * LOGIN PAGE
     */
    @ActionGetMethod("login")
    public ActionResponse loginPage() {

        return new ActionResponse(
                authPageRenderer.loginPage(null)
        );
    }

    /*
     * LOGIN PROCESS
     */
    @ActionPostMethod("login")
    public ActionResponse login(
            HttpServletRequest req
    ) {

        String username =
                req.getParameter("username");

        String password =
                req.getParameter("password");

        User authenticatedUser =
                userService.authenticate(
                        username,
                        password
                );

        /*
         * INVALID LOGIN
         */
        if (authenticatedUser == null) {

            return new ActionResponse(
                    authPageRenderer.loginPage(
                            "Invalid username or password"
                    )
            );
        }

        HttpSession session =
                req.getSession(true);

        /*
         * STORE SESSION
         */
        session.setAttribute(
                "loggedInUser",
                authenticatedUser
        );

        session.setAttribute(
                "username",
                authenticatedUser.getUsername()
        );

        session.setAttribute(
                "role",
                authenticatedUser.getRole()
        );

        /*
         * STUDENT LOGIN
         */
        if (
                "STUDENT".equalsIgnoreCase(
                        authenticatedUser.getRole()
                )
        ) {

            List<Student> students =
                    studentService.findAll();

            for (Student student : students) {

                if (
                        student.getUser() != null
                                &&
                                student.getUser()
                                        .getId()
                                        .equals(
                                                authenticatedUser.getId()
                                        )
                ) {

                    session.setAttribute(
                            "loggedInStudent",
                            student
                    );

                    break;
                }
            }

            return new ActionResponse(
                    new RedirectResponse(
                            "/Mentari/app/student-portal/index"
                    )
            );
        }

        /*
         * ADMIN LOGIN
         */
        return new ActionResponse(
                new RedirectResponse(
                        "/Mentari/app/dashboard/index"
                )
        );
    }

    /*
     * LOGOUT
     */
    @ActionGetMethod("logout")
    public ActionResponse logout(
            HttpServletRequest req
    ) {

        HttpSession session =
                req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return new ActionResponse(
                new RedirectResponse(
                        "/Mentari/"
                )
        );
    }
}