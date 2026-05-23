package app.rest;

import app.bean.DeferralService;
import app.model.Deferral;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/deferral")
public class DeferralRestApi {

    @EJB
    private DeferralService deferralService;

    @Path("/submit") @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submit(Deferral deferral) {
        deferralService.submitRequest(deferral);
        return Response.ok(new ResponseStatus(true, "Deferral submitted successfully")).build();
    }

    @Path("/list") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Deferral> list() { return deferralService.findAll(); }

    @Path("/find/{id}") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Deferral find(@PathParam("id") Long id) { return deferralService.findById(id); }

    @Path("/student/{studentId}") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Deferral> findByStudent(@PathParam("studentId") Long studentId) { return deferralService.findByStudentId(studentId); }

    @Path("/pending/count") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public long countPending() { return deferralService.countPendingDeferrals(); }

    @Path("/approve/{id}/{studentName}") @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response approve(@PathParam("id") Long id, @PathParam("studentName") String studentName) {
        deferralService.approve(id, studentName);
        return Response.ok(new ResponseStatus(true, "Deferral approved")).build();
    }

    @Path("/reject/{id}/{studentName}") @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response reject(@PathParam("id") Long id, @PathParam("studentName") String studentName) {
        deferralService.reject(id, studentName);
        return Response.ok(new ResponseStatus(true, "Deferral rejected")).build();
    }
}