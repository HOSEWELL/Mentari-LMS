package app.dao;

import app.framework.MentariColumn;
import app.framework.MentariTable;
import app.utility.db.DataSourceHelper;

import java.lang.reflect.Field;
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

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            String sql = "SELECT * FROM " + tableName + " ORDER BY " + idField.getAnnotation(MentariColumn.class).name() + " DESC";
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

    public T save(T entity) {
        try (Connection conn = ds.getConnection()) {
            List<String> colNames = new ArrayList<>();
            List<String> placeholders = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : columns) {
                MentariColumn col = field.getAnnotation(MentariColumn.class);
                // Assuming ID is auto-increment for now
                if (col.primaryKey()) continue;

                colNames.add(col.name());
                placeholders.add("?");
                values.add(field.get(entity));
            }

            String sql = "INSERT INTO " + tableName + " (" + String.join(",", colNames) +
                    ") VALUES (" + String.join(",", placeholders) + ")";

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < values.size(); i++) {
                    ps.setObject(i + 1, values.get(i));
                }
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        idField.set(entity, keys.getObject(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private T mapResultSet(ResultSet rs) throws Exception {
        T instance = entityClass.getDeclaredConstructor().newInstance();
        for (Field field : columns) {
            MentariColumn col = field.getAnnotation(MentariColumn.class);
            Object value = rs.getObject(col.name());

            if (value != null) {
                if (field.getType().equals(Long.class) && value instanceof Integer) {
                    value = ((Integer) value).longValue();
                } else if (field.getType().equals(long.class) && value instanceof Integer) {
                    value = ((Integer) value).longValue();
                }

                field.set(instance, value);
            }
        }
        return instance;
    }

    public void setDs(DataSourceHelper ds) {
        this.ds = ds;
    }
}