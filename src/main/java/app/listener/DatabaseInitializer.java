package app.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import app.repository.JdbcRepository;
import app.model.User;
import app.model.Student;
import app.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("###################################################");
        System.out.println("SYSTEM >>> Initializing Mentari Capstone...");
        System.out.println("###################################################");

        try {
            // 1. Sync Tables
            new JdbcRepository<>(User.class).updateSchema();
            new JdbcRepository<>(Student.class).updateSchema();
            System.out.println("[DB ENGINE] Schemas synced successfully.");

            // 2. FIXED: Bootstrap default Admin with existence check
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check if admin already exists first
                String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, "admin");
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            // If count is 0, then we insert
                            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                insertStmt.setString(1, "admin");
                                insertStmt.setString(2, "12345");
                                insertStmt.setString(3, "ADMIN");
                                insertStmt.executeUpdate();
                                System.out.println("BOOTSTRAP >>> Default admin user created.");
                            }
                        } else {
                            System.out.println("BOOTSTRAP >>> Admin user already exists. Skipping.");
                        }
                    }
                }
            }

            System.out.println("SUCCESS >>> Mentari Generic DAO and Singleton are active!");

        } catch (Exception e) {
            System.err.println("CRITICAL >>> Database initialization failed!");
            e.printStackTrace();
        }
    }
}