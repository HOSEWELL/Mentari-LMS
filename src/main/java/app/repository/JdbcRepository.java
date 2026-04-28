package app.repository;

import app.framework.MentariTable;
import app.framework.MentariColumn;
import app.util.DatabaseUtils;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class JdbcRepository<T> implements GenericRepository<T> {

    private final DataSource dataSource;
    private Class<T> type;
    private String tableName;


//     * NO-ARG CONSTRUCTOR Required by the CDI container to create proxies.

    public JdbcRepository() {
        this.dataSource = null;
        this.type = null;
        this.tableName = null;
    }

    /**
     * CONSTRUCTOR INJECTION
     * The container automatically provides the DataSource from your DataSourceProvider.
     */
    @Inject
    public JdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the entity type and determines the table name.
     * Call this in the @Inject constructor or setter of your Action classes.
     */
    public void setType(Class<T> type) {
        this.type = type;
        if (type != null && type.isAnnotationPresent(MentariTable.class)) {
            this.tableName = type.getAnnotation(MentariTable.class).name();
        } else if (type != null) {
            this.tableName = type.getSimpleName().toLowerCase() + "s";
        }
    }

    @Override
    public void updateSchema() {
        if (dataSource == null || type == null) return;
        try (Connection conn = dataSource.getConnection()) {
            DatabaseUtils.createTableIfNotExists(conn, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T save(T entity) {
        if (dataSource == null || type == null) return entity;
        try (Connection conn = dataSource.getConnection()) {
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
                // Check if ID is already set (e.g., for updates or existing records)
                if (idValue != null && (idValue instanceof Long && (Long) idValue > 0)) {
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
        if (dataSource == null || type == null) return list;

        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = dataSource.getConnection();
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

    @Override
    public void update(T entity) {
        if (dataSource == null || type == null) return;
        try (Connection conn = dataSource.getConnection()) {
            StringBuilder setClause = new StringBuilder();
            List<Object> values = new ArrayList<>();
            Field idField = null;
            String idColumnName = "";

            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(MentariColumn.class)) {
                    MentariColumn ann = field.getAnnotation(MentariColumn.class);
                    field.setAccessible(true);

                    if (ann.primaryKey()) {
                        idField = field;
                        idColumnName = ann.name();
                        continue;
                    }

                    setClause.append(ann.name()).append(" = ?, ");
                    values.add(field.get(entity));
                }
            }

            if (setClause.length() > 0 && idField != null) {
                setClause.setLength(setClause.length() - 2);

                String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + idColumnName + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 0; i < values.size(); i++) {
                        ps.setObject(i + 1, values.get(i));
                    }
                    ps.setObject(values.size() + 1, idField.get(entity));
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}