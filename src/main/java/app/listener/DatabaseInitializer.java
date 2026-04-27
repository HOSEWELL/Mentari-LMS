package app.listener;

import app.model.Course;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import app.repository.JdbcRepository;
import app.model.User;
import app.model.Student;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Inject
    private JdbcRepository<Student> studentRepo;

    @Inject
    private JdbcRepository<Course> courseRepo;

    @Inject
    private JdbcRepository<User> userRepo;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        studentRepo.updateSchema();
        courseRepo.updateSchema();
        userRepo.updateSchema();

        System.out.println("[DB ENGINE] Schemas synced successfully.");
    }
}