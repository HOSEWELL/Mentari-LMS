package app.rest;

import app.bean.EnrollmentService;
import app.framework.annotation.AdminOnly;
import app.model.Enrollment;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/enrollment")
@Tag(name = "Enrollment", description = "Enrollment management endpoints")
public class EnrollmentRestApi {

    @EJB
    private EnrollmentService enrollmentService;

    @Path("/enroll")
    @POST
    @AdminOnly
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response enroll(Enrollment enrollment) {
        enrollmentService.enroll(enrollment);
        return Response.ok(new ResponseStatus(true, "Student enrolled successfully")).build();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Enrollment> list() { return enrollmentService.findAll(); }

    @Path("/student/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Enrollment> findByStudent(@PathParam("studentId") Long studentId) {
        return enrollmentService.findByStudentId(studentId);
    }
}