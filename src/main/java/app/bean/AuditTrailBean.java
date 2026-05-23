package app.bean;

import app.dao.AuditTrailDao;
import app.model.AuditTrail;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class AuditTrailBean {

    @Inject
    private AuditTrailDao auditTrailDao;

    public void save(String activity) {

        System.out.println(
                "AUDIT SAVING >>> " + activity
        );

        auditTrailDao.save(
                new AuditTrail(activity)
        );
    }

    public List<AuditTrail> findAll() {
        return auditTrailDao.findAll();
    }
}