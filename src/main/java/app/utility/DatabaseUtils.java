package app.utility;

import app.framework.MentariTable;
import app.framework.MentariColumn;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseUtils {

    public static void createTableIfNotExists(Connection conn, Class<?> modelClass) throws Exception {
        // Check if the class has our custom annotation
        if (!modelClass.isAnnotationPresent(MentariTable.class)) return;

        String tableName = modelClass.getAnnotation(MentariTable.class).name();

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        sql.append("id INT AUTO_INCREMENT PRIMARY KEY, ");

        for (Field field : modelClass.getDeclaredFields()) {
            // Only process fields with our @MentariColumn annotation
            if (field.isAnnotationPresent(MentariColumn.class)) {
                String columnName = field.getAnnotation(MentariColumn.class).name();
                if (columnName.equals("id")) continue;

                String columnType = getSqlType(field.getType());
                sql.append(columnName).append(" ").append(columnType).append(", ");
            }
        }

        sql.setLength(sql.length() - 2); // Remove trailing comma
        sql.append(")");

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql.toString());
            System.out.println("[DB ENGINE] Table '" + tableName + "' synced successfully.");
        }
    }

    private static String getSqlType(Class<?> type) {
        if (type == String.class) return "VARCHAR(255)";
        if (type == int.class || type == Integer.class) return "INT";
        if (type == double.class || type == Double.class) return "DECIMAL(10,2)";
        if (type == boolean.class || type == Boolean.class) return "BOOLEAN";
        return "TEXT";
    }
}