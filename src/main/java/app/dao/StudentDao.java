package app.dao;

import app.model.Student;
import app.utility.bootstrap.InitBootstrap;
import app.utility.db.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class StudentDao extends GenericDao<Student, Long> {

    @Inject
    public StudentDao(@InitBootstrap DataSourceHelper ds) {
        super(Student.class);
        setDs(ds);
    }
}