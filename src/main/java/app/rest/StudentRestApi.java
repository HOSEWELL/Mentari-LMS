package app.rest;

import app.bean.StudentService;
import app.framework.annotation.AdminOnly;
import app.model.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/student")
@Tag(name = "Students", description = "Student management endpoints")
public class StudentRestApi {

    @EJB
    private StudentService studentService;

    @Path("/enroll")
    @POST
    @AdminOnly
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response enroll(Student student) {
        boolean enrolled = studentService.enrollStudent(student);
        if (!enrolled) return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ResponseStatus(false, "Invalid student data")).build();
        return Response.ok(new ResponseStatus(true, "Student enrolled successfully")).build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Student update(Student student) { return studentService.update(student); }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> list() { return studentService.findAll(); }
}