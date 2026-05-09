package app.utility.db;

import app.framework.MentariColumn;
import app.framework.MentariTable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class TableGenerator {

    /**
     * Iterates through all found entities and executes the
     * CREATE TABLE SQL on the provided database connection.
     */
    public static void generateTables(Connection conn, Set<Class<?>> entities) {
        try (Statement stmt = conn.createStatement()) {
            for (Class<?> entity : entities) {
                String sql = generate(entity);
                if (sql != null) {
                    System.out.println("MENTARI SQL >>> Executing: " + sql);
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("MENTARI SQL ERROR >>> Failed to sync database schema.");
            e.printStackTrace();
        }
    }

    /**
     * Generates a CREATE TABLE IF NOT EXISTS statement
     * based on the Mentari annotations.
     */
    public static String generate(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(MentariTable.class)) return null;

        String tableName = clazz.getAnnotation(MentariTable.class).name();
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MentariColumn.class)) {
                MentariColumn col = field.getAnnotation(MentariColumn.class);
                sql.append(col.name()).append(" ").append(getSqlType(field.getType()));

                if (col.primaryKey()) {
                    sql.append(" PRIMARY KEY AUTO_INCREMENT");
                }
                sql.append(", ");
            }
        }

        sql.setLength(sql.length() - 2); // Remove last comma and space
        sql.append(")");
        return sql.toString();
    }

    private static String getSqlType(Class<?> type) {
        if (type == String.class) return "VARCHAR(255)";
        if (type == Long.class || type == int.class || type == Integer.class) return "INT";
        if (type == boolean.class || type == Boolean.class) return "BOOLEAN";
        return "TEXT";
    }
}