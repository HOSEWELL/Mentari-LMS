package app.soap;

import app.bean.DeferralService;
import app.model.Deferral;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService
public class DeferralSoapApi {

    @EJB
    private DeferralService deferralService;

    @WebMethod
    public void submitRequest(
            @WebParam(name = "deferral") Deferral deferral
    ) {

        deferralService.submitRequest(deferral);
    }

    @WebMethod
    public void approve(
            @WebParam(name = "id") Long id,
            @WebParam(name = "studentName") String studentName
    ) {

        deferralService.approve(id, studentName);
    }

    @WebMethod
    public void reject(
            @WebParam(name = "id") Long id,
            @WebParam(name = "studentName") String studentName
    ) {

        deferralService.reject(id, studentName);
    }

    @WebMethod
    @WebResult(name = "deferrals")
    public DeferralWrapper list() {

        return new DeferralWrapper(
                deferralService.findAll()
        );
    }
}