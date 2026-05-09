package app.utility.db;

import app.utility.bootstrap.InitBootstrap;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@InitBootstrap
@ApplicationScoped
public class DataSourceHelper {

    @Resource(lookup = "java:jboss/datasources/MentariDS")
    private DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}