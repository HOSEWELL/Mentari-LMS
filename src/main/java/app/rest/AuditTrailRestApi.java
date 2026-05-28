package app.rest;

import app.bean.AuditTrailBean;
import app.framework.annotation.AdminOnly;
import app.model.AuditTrail;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/audit")
@Tag(name = "Audit", description = "Audit management endpoints")
@AdminOnly
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