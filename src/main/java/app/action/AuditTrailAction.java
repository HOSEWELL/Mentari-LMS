package app.action;

import app.bean.AuditTrailBean;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ProtectedRoute;

import app.framework.response.ActionResponse;

import app.model.AuditTrail;

import jakarta.inject.Inject;

@Action(
        value = "audit-trail",
        label = "Audit Trail",
        roles = {"ADMIN"},
        showLink = true
)

@ProtectedRoute(
        roles = {"ADMIN"}
)
public class AuditTrailAction {

    @Inject
    private AuditTrailBean auditTrailBean;

    @ActionGetMethod("list")
    public ActionResponse list() {

        return new ActionResponse(
                AuditTrail.class,
                auditTrailBean.findAll()
        );
    }
}