package app.action;

import app.bean.CourseService;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.ActionRequestBody;
import app.framework.annotation.ProtectedRoute;

import app.framework.response.ActionResponse;

import app.model.Course;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped

@Action(
        value = "courses",
        label = "Courses",
        roles = {"ADMIN"},
        showLink = true
)

@ProtectedRoute(
        roles = {"ADMIN"}
)
public class CourseAction {

    @EJB
    private CourseService courseService;

    /*
     * LIST COURSES
     */
    @ActionGetMethod("list")
    public ActionResponse list() {

        return new ActionResponse(
                Course.class,
                courseService.findAll()
        );
    }

    /*
     * SAVE COURSE
     */
    @ActionPostMethod("save")
    public ActionResponse save(

            @ActionRequestBody
            Course course
    ) {

        courseService.save(course);

        return new ActionResponse(
                Course.class,
                courseService.findAll()
        );
    }
}