package app.dao;

import app.model.Course;
import jakarta.enterprise.context.Dependent;

@Dependent
public class CourseDao extends GenericDao<Course, Long> {
}