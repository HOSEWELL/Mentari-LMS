package app.action;

import app.bean.CourseService;
import app.bean.DeferralService;
import app.bean.StudentService;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;

import app.framework.response.ActionResponse;
import app.framework.response.DashboardCard;
import app.framework.response.DashboardResponse;

import app.listener.AppSessionListener;

import jakarta.ejb.EJB;

import java.util.List;

@Action(
        value = "dashboard",
        label = "Dashboard",
        roles = {"ADMIN"},
        showLink = true
)
public class DashboardAction {

    @EJB
    private StudentService studentService;

    @EJB
    private CourseService courseService;

    @EJB
    private DeferralService deferralService;

    @ActionGetMethod("index")
    public ActionResponse dashboard() {

        DashboardResponse response =
                new DashboardResponse(
                        "Mentari Dashboard",
                        List.of(

                                new DashboardCard(
                                        "Total Students",
                                        String.valueOf(
                                                studentService
                                                        .findAll()
                                                        .size()
                                        ),
                                        "fas fa-users",
                                        "#2563eb"
                                ),

                                new DashboardCard(
                                        "Courses",
                                        String.valueOf(
                                                courseService
                                                        .findAll()
                                                        .size()
                                        ),
                                        "fas fa-book",
                                        "#16a34a"
                                ),

                                new DashboardCard(
                                        "Pending Deferrals",
                                        String.valueOf(
                                                deferralService
                                                        .countPendingDeferrals()
                                        ),
                                        "fas fa-clock",
                                        "#f59e0b"
                                ),

                                new DashboardCard(
                                        "Active Sessions",
                                        String.valueOf(
                                                AppSessionListener
                                                        .getActiveSessionCount()
                                        ),
                                        "fas fa-signal",
                                        "#dc2626"
                                )
                        )
                );

        return new ActionResponse(response);
    }
}