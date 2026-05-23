package app.soap;

import app.bean.CourseService;
import app.model.Course;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService
public class CourseSoapApi {

    @EJB
    private CourseService courseService;

    @WebMethod
    public boolean save(
            @WebParam(name = "course") Course course
    ) {

        return courseService.save(course);
    }

    @WebMethod
    @WebResult(name = "courses")
    public CourseWrapper list() {

        return new CourseWrapper(
                courseService.findAll()
        );
    }
}