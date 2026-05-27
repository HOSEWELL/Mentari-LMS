package app.action;

import app.bean.EnrollmentService;
import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.ProtectedRoute;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import app.model.Course;
import app.model.Enrollment;
import app.model.Student;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Action(value = "enrollments", label = "Enrollments", roles = {"ADMIN", "STUDENT"}, showLink = true)
public class EnrollmentAction {

    @Inject
    private EnrollmentService enrollmentService;

    @ActionGetMethod("list")
    @ProtectedRoute(roles = {"ADMIN"})
    public ActionResponse list() {
        return new ActionResponse(Enrollment.class, enrollmentService.findAll());
    }

    @ActionGetMethod("admin")
    @ProtectedRoute(roles = {"ADMIN"})
    public ActionResponse admin() {
        return new ActionResponse(Enrollment.class, enrollmentService.findAll());
    }

    @ActionGetMethod("student")
    @ProtectedRoute(roles = {"STUDENT"})
    public ActionResponse student(HttpSession session) {
        Student loggedStudent = (Student) session.getAttribute("loggedInStudent");
        List<Enrollment> enrollments = enrollmentService.findByStudentId(loggedStudent.getId());
        StringBuilder html = new StringBuilder();

        html.append("<div class='student-course-page'><h1 class='page-title'>My Courses</h1><div class='course-grid'>");

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            html.append("""
<div class='course-card'>
<div class='course-header'><h2>%s</h2><span class='course-code'>%s</span></div>
<p class='course-description'>%s</p>
<div class='course-footer'><i class="fas fa-book-open"></i><span>Enrolled Course</span></div>
</div>
""".formatted(course.getName(), course.getCode(), course.getDescription()));
        }

        html.append("""
</div></div>
<style>
.student-course-page{ padding:10px 0; }
.course-grid{ display:grid; grid-template-columns:repeat(auto-fit,minmax(320px,1fr)); gap:24px; margin-top:25px; }
.course-card{ background:white; border-radius:20px; padding:28px; box-shadow:0 8px 24px rgba(0,0,0,0.06); border:1px solid #e5e7eb; transition:0.25s; }
.course-card:hover{ transform:translateY(-6px); box-shadow:0 18px 35px rgba(0,0,0,0.1); }
.course-header{ display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
.course-header h2{ font-size:22px; color:#111827; }
.course-code{ background:#2563eb; color:white; padding:8px 14px; border-radius:999px; font-size:13px; font-weight:600; }
.course-description{ color:#6b7280; line-height:1.7; font-size:15px; margin-bottom:25px; }
.course-footer{ display:flex; align-items:center; gap:10px; color:#2563eb; font-weight:600; font-size:14px; }
</style>
""");

        return new ActionResponse(html.toString());
    }

    @ActionPostMethod("enroll")
    @ProtectedRoute(roles = {"ADMIN"})
    public RedirectResponse enroll(Enrollment enrollment) {
        enrollmentService.enroll(enrollment);
        return new RedirectResponse("/Mentari/app/enrollments/list");
    }
}