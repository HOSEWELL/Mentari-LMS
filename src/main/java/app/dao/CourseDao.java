package app.dao;

import app.model.Course;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class CourseDao {

    @Inject
    private DataSource dataSource;

    public Course save(Course course) {
        String sql = "INSERT INTO courses (course_name, course_code, description) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getName());
            ps.setString(2, course.getCode());
            ps.setString(3, course.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) course.setId(keys.getLong(1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return course;
    }

    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("course_name"));
                c.setCode(rs.getString("course_code"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}