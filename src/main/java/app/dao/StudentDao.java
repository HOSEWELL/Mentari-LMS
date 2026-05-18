package app.dao;

import app.model.Student;
import jakarta.enterprise.context.Dependent;

@Dependent
public class StudentDao extends GenericDao<Student, Long> {
}