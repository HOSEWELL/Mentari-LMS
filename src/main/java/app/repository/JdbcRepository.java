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
    public T save(T entity) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Field idField = null;
            for (Field f : type.getDeclaredFields()) {
                if (f.isAnnotationPresent(MentariColumn.class) && f.getAnnotation(MentariColumn.class).primaryKey()) {
                    idField = f;
                    break;
                }
            }

            if (idField != null) {
                idField.setAccessible(true);
                Object idValue = idField.get(entity);
                if (idValue != null) {
                    System.out.println("Record with ID " + idValue + " already exists. Skipping insert.");
                    return entity;
                }
            }

            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            List<Object> values = new ArrayList<>();

            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(MentariColumn.class)) {
                    MentariColumn ann = field.getAnnotation(MentariColumn.class);
                    if (ann.primaryKey()) continue;

                    field.setAccessible(true);
                    columns.append(ann.name()).append(",");
                    placeholders.append("?").append(",");
                    values.add(field.get(entity));
                }
            }

            if (columns.length() > 0) {
                columns.setLength(columns.length() - 1);
                placeholders.setLength(placeholders.length() - 1);

                String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    for (int i = 0; i < values.size(); i++) {
                        ps.setObject(i + 1, values.get(i));
                    }
                    ps.executeUpdate();

                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next() && idField != null) {
                            long generatedId = generatedKeys.getLong(1);
                            idField.set(entity, generatedId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
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
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Updates an existing record in the database.
     * Required for enabling password changes in the Student Portal.
     */
    @Override
    public void update(T entity) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder setClause = new StringBuilder();
            List<Object> values = new ArrayList<>();
            Field idField = null;
            String idColumnName = "";

            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(MentariColumn.class)) {
                    MentariColumn ann = field.getAnnotation(MentariColumn.class);
                    field.setAccessible(true);

                    // Identify the Primary Key for the WHERE clause
                    if (ann.primaryKey()) {
                        idField = field;
                        idColumnName = ann.name();
                        continue;
                    }

                    // Add other columns to the SET clause
                    setClause.append(ann.name()).append(" = ?, ");
                    values.add(field.get(entity));
                }
            }

            if (setClause.length() > 0 && idField != null) {
                // Remove trailing comma and space
                setClause.setLength(setClause.length() - 2);

                String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + idColumnName + " = ?";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    // Inject values for the SET clause
                    for (int i = 0; i < values.size(); i++) {
                        ps.setObject(i + 1, values.get(i));
                    }
                    // Inject the Primary Key value for the WHERE clause
                    ps.setObject(values.size() + 1, idField.get(entity));

                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}