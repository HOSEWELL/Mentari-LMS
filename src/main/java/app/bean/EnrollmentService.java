package app.bean;

import app.dao.EnrollmentDao;
import app.model.Enrollment;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class EnrollmentService {

    @Inject
    private EnrollmentDao enrollmentDao;

    @EJB
    private AuditTrailBean auditTrailBean;

    public void enroll(Enrollment enrollment) {

        enrollmentDao.save(enrollment);

        auditTrailBean.save(
                "Student enrolled to course: "
                        + enrollment.getStudent().getFullName()
                        + " -> "
                        + enrollment.getCourse().getName()
        );
    }

    public List<Enrollment> findAll() {
        return enrollmentDao.findAll();
    }

    public List<Enrollment> findByStudentId(Long studentId) {

        return enrollmentDao.findAll()
                .stream()
                .filter(enrollment ->
                        enrollment.getStudent() != null
                                &&
                                enrollment.getStudent()
                                        .getId()
                                        .equals(studentId)
                )
                .toList();
    }
}