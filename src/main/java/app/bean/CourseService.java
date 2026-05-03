package app.bean;

import app.dao.CourseDao;
import app.model.Course;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class CourseService {

    @Inject
    private CourseDao courseDao;

    public Course save(Course course) {
        return courseDao.save(course);
    }

    public List<Course> findAll() {
        return courseDao.findAll();
    }
}