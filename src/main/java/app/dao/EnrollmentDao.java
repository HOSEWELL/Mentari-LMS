package app.dao;

import app.model.Enrollment;
import jakarta.enterprise.context.Dependent;

@Dependent
public class EnrollmentDao extends GenericDao<Enrollment, Long> {
}