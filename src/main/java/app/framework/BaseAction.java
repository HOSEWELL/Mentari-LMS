package app.framework;

import jakarta.servlet.http.HttpServlet;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public abstract class BaseAction<T> extends HttpServlet {

    /**
     * Converts HTTP form parameters into an instance of the generic entity type T.
     */
    @SuppressWarnings("unchecked")
    protected T serializeForm(Map<String, String[]> parameterMap) {
        try {
            // Determine the actual generic type at runtime.
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];

            // Create a new instance using the no-argument constructor.
            T instance = entityClass.getDeclaredConstructor().newInstance();

            // 3. Loop through all declared fields of the entity.
            for (Field field : entityClass.getDeclaredFields()) {

                // 4. Check if the HTTP request contains a parameter with the
                //    same name as the field.
                if (parameterMap.containsKey(field.getName())) {

                    // Allow access to private fields.
                    field.setAccessible(true);

                    // Extract the submitted value.
                    // parameterMap stores values as String[] because a form field may have multiple values.
                    String value = parameterMap.get(field.getName())[0];

                    // Ignore null or blank values.
                    if (value == null || value.trim().isEmpty()) {
                        continue;
                    }

                    // Convert the String value to the correct field type.
                    Class<?> fieldType = field.getType();

                    if (fieldType == Long.class || fieldType == long.class) {
                        field.set(instance, Long.parseLong(value));

                    } else if (fieldType == Integer.class || fieldType == int.class) {
                        field.set(instance, Integer.parseInt(value));

                    } else if (fieldType == Double.class || fieldType == double.class) {
                        field.set(instance, Double.parseDouble(value));

                    } else if (fieldType == Float.class || fieldType == float.class) {
                        field.set(instance, Float.parseFloat(value));

                    } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                        field.set(instance, Boolean.parseBoolean(value));

                    } else {
                        // Default case: assign as String
                        field.set(instance, value);
                    }
                }
            }

            // 6. Return the populated object.
            return instance;

        } catch (Exception e) {
            System.err.println("FRAMEWORK >>> Serialization Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}