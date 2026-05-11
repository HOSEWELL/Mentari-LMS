package app.bean;

import app.dao.DeferralDao;
import app.model.Deferral;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

@Stateless
public class DeferralService {

    @Inject
    private DeferralDao deferralDao;

    @EJB
    private AuditTrailBean auditTrailBean;

    public long countPendingDeferrals() {
        return deferralDao.findAll()
                .stream()
                .filter(d -> "PENDING".equalsIgnoreCase(d.getStatus()))
                .count();
    }

    public void submitRequest(Deferral deferral) {
        deferral.setStatus("PENDING");
        deferral.setSubmittedAt(new Date().toString());

        deferralDao.save(deferral);

        auditTrailBean.save("Deferral submitted by: " + deferral.getStudentName());
    }

    public List<Deferral> findAll() {
        // Inherited from GenericDao
        return deferralDao.findAll();
    }

    public List<Deferral> findByStudentId(Long studentId) {
        // Filter using GenericDao.findAll()
        return deferralDao.findAll()
                .stream()
                .filter(deferral ->
                        deferral.getStudentId() != null
                                && deferral.getStudentId().equals(studentId))
                .toList();
    }

    public void approve(Long id, String studentName) {
        Deferral deferral = findById(id);

        if (deferral != null) {
            deferral.setStatus("APPROVED");
            deferralDao.update(deferral);

            auditTrailBean.save("Deferral APPROVED for: " + studentName);
        }
    }

    public void reject(Long id, String studentName) {
        Deferral deferral = findById(id);

        if (deferral != null) {
            deferral.setStatus("REJECTED");
            deferralDao.update(deferral);

            auditTrailBean.save("Deferral REJECTED for: " + studentName);
        }
    }

    private Deferral findById(Long id) {
        return deferralDao.findAll()
                .stream()
                .filter(deferral ->
                        deferral.getId() != null
                                && deferral.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}