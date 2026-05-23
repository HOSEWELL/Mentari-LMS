package app.rest;

import app.bean.CourseService;
import app.model.Course;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/course")
public class CourseRestApi {

    @EJB
    private CourseService courseService;

    @Path("/save") @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Course course) {
        boolean saved = courseService.save(course);
        if (!saved) return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseStatus(false, "Invalid course data")).build();
        return Response.ok(new ResponseStatus(true, "Course saved successfully")).build();
    }

    @Path("/list") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> list() {
        return courseService.findAll();
    }
}