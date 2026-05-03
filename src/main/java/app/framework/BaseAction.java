package app.framework;

import app.utility.validation.Validate;
import jakarta.servlet.http.HttpServlet;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public abstract class BaseAction<T> extends HttpServlet {

    @SuppressWarnings("unchecked")
    public T serializeForm(Map<String, String[]> parameterMap, Validate<String> validator) {
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];

            T instance = entityClass.getDeclaredConstructor().newInstance();

            for (Field field : entityClass.getDeclaredFields()) {
                if (parameterMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    String value = parameterMap.get(field.getName())[0];

                    // --- NEW VALIDATION LOGIC ---
                    // If this is the 'name' or 'full_name' field, run it through the validator
                    if (field.getName().toLowerCase().contains("name") && validator != null) {
                        if (!validator.name(value)) {
                            System.err.println("FRAMEWORK >>> Validation Failed for: " + value);
                            return null; // Stop serialization if validation fails
                        }
                    }

                    // Type conversion
                    if (field.getType() == Long.class || field.getType() == long.class) {
                        field.set(instance, Long.parseLong(value));
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        field.set(instance, Integer.parseInt(value));
                    } else {
                        field.set(instance, value);
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            System.err.println("FRAMEWORK >>> Serialization Error: " + e.getMessage());
            return null;
        }
    }
}