package app.dao;

import app.model.Course;
import app.utility.bootstrap.InitBootstrap;
import app.utility.db.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class CourseDao extends GenericDao<Course, Long> {

    @Inject
    public CourseDao(@InitBootstrap DataSourceHelper ds) {
        super(Course.class);
        setDs(ds);
    }

}