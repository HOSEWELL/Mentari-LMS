package app.dao;

import app.framework.MentariColumn;
import app.framework.MentariTable;
import app.utility.db.DataSourceHelper;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

public abstract class GenericDao<T, ID> {

    private final Class<T> entityClass;
    private final String tableName;
    private final List<Field> columns = new ArrayList<>();
    private Field idField;

    protected DataSourceHelper ds;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;

        if (!entityClass.isAnnotationPresent(MentariTable.class)) {
            throw new RuntimeException("Missing @MentariTable on " + entityClass.getName());
        }

        this.tableName = entityClass.getAnnotation(MentariTable.class).name();

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MentariColumn.class)) {
                field.setAccessible(true);
                columns.add(field);
                if (field.getAnnotation(MentariColumn.class).primaryKey()) {
                    idField = field;
                }
            }
        }
    }

    // ---------------------------
    // READ ALL
    // ---------------------------
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            String sql = "SELECT * FROM " + tableName
                    + " ORDER BY " + idField.getAnnotation(MentariColumn.class).name() + " DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------------------
    // SAVE
    // ---------------------------
    public T save(T entity) {
        try (Connection conn = ds.getConnection()) {
            List<String> colNames = new ArrayList<>();
            List<String> placeholders = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : columns) {
                MentariColumn col = field.getAnnotation(MentariColumn.class);
                if (col.primaryKey()) continue;
                colNames.add(col.name());
                placeholders.add("?");
                values.add(field.get(entity));
            }

            String sql = "INSERT INTO " + tableName
                    + " (" + String.join(",", colNames) + ")"
                    + " VALUES (" + String.join(",", placeholders) + ")";

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < values.size(); i++) {
                    ps.setObject(i + 1, values.get(i));
                }
                ps.executeUpdate();

                // Fix: handle BigInteger/Integer generated keys
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next() && idField != null) {
                        Object key = keys.getObject(1);
                        idField.set(entity, toLong(key));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    // ---------------------------
    // UPDATE
    // ---------------------------
    public void update(T entity) {
        try (Connection conn = ds.getConnection()) {
            List<String> setParts = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            String idColumn = idField.getAnnotation(MentariColumn.class).name();
            Object idValue = idField.get(entity);

            for (Field field : columns) {
                MentariColumn col = field.getAnnotation(MentariColumn.class);
                if (col.primaryKey()) continue;
                setParts.add(col.name() + " = ?");
                values.add(field.get(entity));
            }

            String sql = "UPDATE " + tableName
                    + " SET " + String.join(", ", setParts)
                    + " WHERE " + idColumn + " = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.size(); i++) {
                    ps.setObject(i + 1, values.get(i));
                }
                ps.setObject(values.size() + 1, idValue);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    // DELETE
    // ---------------------------
    public void delete(ID id) {
        try (Connection conn = ds.getConnection()) {
            String idColumn = idField.getAnnotation(MentariColumn.class).name();
            String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    // MAPPER
    // ---------------------------
    private T mapResultSet(ResultSet rs) throws Exception {
        T instance = entityClass.getDeclaredConstructor().newInstance();

        for (Field field : columns) {
            MentariColumn col = field.getAnnotation(MentariColumn.class);
            Object value = rs.getObject(col.name());

            if (value == null) continue;

            // Fix: handle all numeric type mismatches MySQL can return
            if (field.getType() == Long.class || field.getType() == long.class) {
                value = toLong(value);
            } else if (field.getType() == Integer.class || field.getType() == int.class) {
                value = toInt(value);
            }

            field.set(instance, value);
        }

        return instance;
    }

    // ---------------------------
    // TYPE HELPERS
    // ---------------------------

    // Converts Integer, Long, or BigInteger → Long safely
    private Long toLong(Object value) {
        if (value instanceof Long)       return (Long) value;
        if (value instanceof Integer)    return ((Integer) value).longValue();
        if (value instanceof BigInteger) return ((BigInteger) value).longValue();
        return Long.parseLong(value.toString());
    }

    // Converts Long or BigInteger → Integer safely
    private Integer toInt(Object value) {
        if (value instanceof Integer)    return (Integer) value;
        if (value instanceof Long)       return ((Long) value).intValue();
        if (value instanceof BigInteger) return ((BigInteger) value).intValue();
        return Integer.parseInt(value.toString());
    }

    public void setDs(DataSourceHelper ds) {
        this.ds = ds;
    }
}