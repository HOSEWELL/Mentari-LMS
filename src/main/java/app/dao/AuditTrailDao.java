package app.dao;

import app.model.AuditTrail;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class AuditTrailDao {

    @Inject
    private DataSource dataSource;

    public void save(AuditTrail trail) {
        String sql = "INSERT INTO audit_trail (activity, created_at) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trail.getActivity());
            ps.setString(2, trail.getCreatedAt());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<AuditTrail> findAll() {
        List<AuditTrail> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_trail ORDER BY id DESC";
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                AuditTrail t = new AuditTrail();
                t.setId(rs.getLong("id"));
                t.setActivity(rs.getString("activity"));
                t.setCreatedAt(rs.getString("created_at"));
                list.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}