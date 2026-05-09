package app.dao;

import app.model.User;
import app.utility.bootstrap.InitBootstrap;
import app.utility.db.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Dependent
public class UserDao extends GenericDao<User, Long> {

    @Inject
    public UserDao(@InitBootstrap DataSourceHelper ds) {
        super(User.class);
        // This is the critical line: it populates the 'ds' field in GenericDao
        this.ds = ds;
    }

    public void update(User user) {
        String sql = "UPDATE users SET username=?, password=?, role=? WHERE id=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}