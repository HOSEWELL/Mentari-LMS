package app.action;

import app.bean.DeferralService;
import app.framework.annotation.*;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import app.model.Deferral;
import app.model.Student;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;

@Action(
        value = "deferrals",
        label = "Deferrals",
        roles = {"ADMIN", "STUDENT"},
        showLink = true
)
public class DeferralAction {

    @Inject
    private DeferralService deferralService;

    /*
     * =========================
     * ADMIN VIEW
     * =========================
     */
    @ActionGetMethod("admin")
    @ProtectedRoute(roles = {"ADMIN"})
    public ActionResponse admin() {

        return new ActionResponse(
                Deferral.class,
                deferralService.findAll()
        );
    }

    /*
     * =========================
     * STUDENT VIEW
     * =========================
     */
    @ActionGetMethod("student")
    @ProtectedRoute(roles = {"STUDENT"})
    public ActionResponse student(
            HttpSession session
    ) {

        Student loggedStudent =
                (Student) session.getAttribute(
                        "loggedInStudent"
                );

        return new ActionResponse(
                Deferral.class,
                deferralService.findByStudentId(
                        loggedStudent.getId()
                )
        );
    }

    /*
     * =========================
     * SUBMIT REQUEST
     * =========================
     */
    @ActionPostMethod("submit")
    @ProtectedRoute(roles = {"STUDENT"})
    public RedirectResponse submit(
            @ActionRequestBody
            Deferral deferral,

            HttpSession session
    ) {

        Student loggedStudent =
                (Student) session.getAttribute(
                        "loggedInStudent"
                );

        deferral.setStudent(loggedStudent);

        deferralService.submitRequest(deferral);

        return new RedirectResponse(
                "/Mentari/app/deferrals/student"
        );
    }

    /*
     * =========================
     * APPROVE
     * =========================
     */
    @ActionGetMethod("approve/{id}")
    @ProtectedRoute(roles = {"ADMIN"})
    public RedirectResponse approve(
            @ActionPathParam("id")
            Long id
    ) {

        Deferral deferral =
                deferralService.findById(id);

        if (deferral != null) {

            deferralService.approve(
                    id,
                    deferral.getStudent().getFullName()
            );
        }

        return new RedirectResponse(
                "/Mentari/app/deferrals/admin"
        );
    }

    /*
     * =========================
     * REJECT
     * =========================
     */
    @ActionGetMethod("reject/{id}")
    @ProtectedRoute(roles = {"ADMIN"})
    public RedirectResponse reject(
            @ActionPathParam("id")
            Long id
    ) {

        Deferral deferral =
                deferralService.findById(id);

        if (deferral != null) {

            deferralService.reject(
                    id,
                    deferral.getStudent().getFullName()
            );
        }

        return new RedirectResponse(
                "/Mentari/app/deferrals/admin"
        );
    }
}