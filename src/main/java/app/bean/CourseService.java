package app.bean;

import app.dao.CourseDao;
import app.model.Course;
import app.utility.validation.Validate;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Stateless
public class CourseService {

    @Inject
    private CourseDao courseDao;

    @Inject
    @Named("Course")
    private Validate<String> courseValidator;

    public boolean save(Course course) {
        if (course == null || !courseValidator.name(course.getName())) {
            return false;
        }

        courseDao.save(course);
        return true;
    }

    public List<Course> findAll() {
        return courseDao.findAll();
    }
}