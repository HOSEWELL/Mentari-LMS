package app.database;

import app.model.Course;
import app.model.Student;
import app.model.User;
import app.repository.JdbcRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import javax.sql.DataSource; // Added import

@ApplicationScoped
public class RepositoryProvider {

    @Inject
    private DataSource dataSource;

    @Produces
    @ApplicationScoped
    public JdbcRepository<Student> produceStudentRepo() {
        // Manually passing the injected dataSource into the constructor
        return new JdbcRepository<>(Student.class, dataSource);
    }

    @Produces
    @ApplicationScoped
    public JdbcRepository<Course> produceCourseRepo() {
        return new JdbcRepository<>(Course.class, dataSource);
    }

    @Produces
    @ApplicationScoped
    public JdbcRepository<User> produceUserRepo() {
        return new JdbcRepository<>(User.class, dataSource);
    }
} 