package app.bean;

import app.dao.AuditTrailDao;
import app.model.AuditTrail;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import java.util.List;

@Singleton
public class AuditTrailBean {

    @Inject
    private AuditTrailDao auditTrailDao;

    public void save(String activity) {
        auditTrailDao.save(new AuditTrail(activity));
    }

    public List<AuditTrail> findAll() {
        return auditTrailDao.findAll();
    }
}