package app.action;

import app.bean.CourseService;
import app.framework.annotation.*;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import app.model.Course;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Action(value = "courses", label = "Courses", roles = {"ADMIN"}, showLink = true)
@ProtectedRoute(roles = {"ADMIN"})
public class CourseAction {

    @EJB
    private CourseService courseService;

    @ActionGetMethod("list")
    public ActionResponse list() {
        return new ActionResponse(Course.class, courseService.findAll());
    }

    @ActionPostMethod("save")
    public RedirectResponse save(@ActionRequestBody Course course) {
        courseService.save(course);
        return new RedirectResponse("/Mentari/app/courses/list");
    }
}