package app.rest;

import app.bean.AuditTrailBean;
import app.model.AuditTrail;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/audit")
public class AuditTrailRestApi {

    @EJB
    private AuditTrailBean auditTrailBean;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditTrail> list() {

        return auditTrailBean.findAll();
    }

}