package app.listener;

import app.model.Course;
import app.model.Student;
import app.model.User;
import app.utility.DatabaseUtils;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Inject
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseUtils.createTableIfNotExists(conn, User.class);
            DatabaseUtils.createTableIfNotExists(conn, Student.class);
            DatabaseUtils.createTableIfNotExists(conn, Course.class);
            System.out.println("[DB ENGINE] Schemas synced successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}