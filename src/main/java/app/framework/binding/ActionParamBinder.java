package app.framework.binding;

import app.framework.annotation.ActionPathParam;
import app.framework.annotation.ActionQueryParam;
import app.framework.annotation.ActionRequestBody;
import app.framework.routing.ActionMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;

public class ActionParamBinder {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object[] bind(ActionMap actionMap, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) throws Exception {

        Parameter[] parameters = actionMap.getMethod().getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.getType() == HttpServletRequest.class) { args[i] = req; continue; }
            if (parameter.getType() == HttpServletResponse.class) { args[i] = resp; continue; }
            if (parameter.getType() == HttpSession.class) { args[i] = req.getSession(); continue; }

            if (parameter.isAnnotationPresent(ActionQueryParam.class)) {
                args[i] = convert(req.getParameter(parameter.getAnnotation(ActionQueryParam.class).value()), parameter.getType());
                continue;
            }

            if (parameter.isAnnotationPresent(ActionPathParam.class)) {
                args[i] = convert(pathVariables.get(parameter.getAnnotation(ActionPathParam.class).value()), parameter.getType());
                continue;
            }

            if (parameter.isAnnotationPresent(ActionRequestBody.class)) {
                String contentType = req.getContentType();
                if (contentType != null && contentType.contains("application/json")) {
                    args[i] = mapper.readValue(req.getInputStream(), parameter.getType());
                } else {
                    args[i] = bindForm(req, parameter.getType());
                }
                continue;
            }

            args[i] = bindForm(req, parameter.getType());
        }

        return args;
    }

    private static Object bindForm(HttpServletRequest req, Class<?> clazz) throws Exception {
        Object object = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String nestedId = req.getParameter(field.getName() + ".id");

            if (nestedId != null && !nestedId.isBlank()) {
                Object nestedObject = field.getType().getDeclaredConstructor().newInstance();
                Field idField = nestedObject.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(nestedObject, Long.parseLong(nestedId));
                field.set(object, nestedObject);
                continue;
            }

            String value = req.getParameter(field.getName());
            if (value == null || value.isBlank()) continue;
            field.set(object, convert(value, field.getType()));
        }

        return object;
    }

    private static Object convert(String value, Class<?> type) {

        if (value == null || value.isBlank()) {
            return null;
        }

        if (type == String.class) {
            return value;
        }

        if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        }

        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }

        if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        }

        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        }

        if (type == java.time.LocalDate.class) {
            return java.time.LocalDate.parse(value);
        }

        if (type == java.time.LocalDateTime.class) {
            return java.time.LocalDateTime.parse(value);
        }

        return value;
    }
}