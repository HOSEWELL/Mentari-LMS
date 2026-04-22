package app.repository;

import app.database.DatabaseConnection;
import app.framework.MentariTable;
import app.framework.MentariColumn;
import app.util.DatabaseUtils;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcRepository<T> implements GenericRepository<T> {
    private final Class<T> type;
    private final String tableName;

    public JdbcRepository(Class<T> type) {
        this.type = type;
        if (type.isAnnotationPresent(MentariTable.class)) {
            this.tableName = type.getAnnotation(MentariTable.class).name();
        } else {
            this.tableName = type.getSimpleName().toLowerCase() + "s";
        }
    }

    @Override
    public void updateSchema() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseUtils.createTableIfNotExists(conn, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(T entity) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Primary Key Check to prevent duplicates
            Field idField = null;
            for (Field f : type.getDeclaredFields()) {
                // Matches the corrected annotation attribute name 'primaryKey'
                if (f.isAnnotationPresent(MentariColumn.class) && f.getAnnotation(MentariColumn.class).primaryKey()) {
                    idField = f;
                    break;
                }
            }

            if (idField != null) {
                idField.setAccessible(true);
                Object idValue = idField.get(entity);
                // If ID is already set, we stop the save to prevent duplicate INSERTs
                // In a full production app, you would add an UPDATE query here instead
                if (idValue != null) {
                    System.out.println("Record with ID " + idValue + " already exists. Skipping insert.");
                    return;
                }
            }

            // 2. Prepare Insert Logic
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            List<Object> values = new ArrayList<>();

            // Inside your save method - make sure you aren't trying to insert 'id'
// Change this part to ensure it uses the ANNOTATION name, not the FIELD name
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(MentariColumn.class)) {
                    MentariColumn ann = field.getAnnotation(MentariColumn.class);
                    if (ann.primaryKey()) continue; // Skip ID

                    field.setAccessible(true);
                    columns.append(ann.name()).append(","); // Use ann.name()
                    placeholders.append("?").append(",");
                    values.add(field.get(entity));
                }
            }

            // Remove trailing commas
            if (columns.length() > 0) {
                columns.setLength(columns.length() - 1);
                placeholders.setLength(placeholders.length() - 1);

                String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 0; i < values.size(); i++) {
                        ps.setObject(i + 1, values.get(i));
                    }
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    if (field.isAnnotationPresent(MentariColumn.class)) {
                        String colName = field.getAnnotation(MentariColumn.class).name();
                        field.setAccessible(true);

                        // Handle potential type mismatches (Long vs Int)
                        Object value = rs.getObject(colName);
                        if (value != null) {
                            if (field.getType() == Long.class && value instanceof Integer) {
                                field.set(instance, ((Integer) value).longValue());
                            } else {
                                field.set(instance, value);
                            }
                        }
                    }
                }
                list.add(instance);
            }
        } catch (Exception e) {
            System.err.println("Error in findAll for " + tableName);
            e.printStackTrace();
        }
        return list;
    }
}