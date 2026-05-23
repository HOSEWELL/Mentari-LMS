package app.soap;

import app.bean.AuditTrailBean;
import app.model.AuditTrail;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService
public class AuditTrailSoapApi {

    @EJB
    private AuditTrailBean auditTrailBean;

    @WebMethod
    public void save(String activity) {

        auditTrailBean.save(activity);
    }

    @WebMethod
    @WebResult(name = "auditTrails")
    public AuditTrailWrapper list() {

        return new AuditTrailWrapper(
                auditTrailBean.findAll()
        );
    }
}