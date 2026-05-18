package app.framework.binding;

import app.framework.annotation.ActionPathParam;
import app.framework.annotation.ActionRequestBody;
import app.framework.annotation.ActionQueryParam;
import app.framework.routing.ActionMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;

public class ActionParamBinder {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    public static Object[] bind(
            ActionMap actionMap,
            HttpServletRequest req,
            HttpServletResponse resp,
            Map<String, String> pathVariables
    ) throws Exception {

        Parameter[] parameters =
                actionMap
                        .getMethod()
                        .getParameters();

        Object[] args =
                new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            Parameter parameter =
                    parameters[i];

            /*
             * HttpServletRequest Injection
             */
            if (parameter.getType()
                    == HttpServletRequest.class) {

                args[i] = req;

                continue;
            }

            /*
             * HttpServletResponse Injection
             */
            if (parameter.getType()
                    == HttpServletResponse.class) {

                args[i] = resp;

                continue;
            }

            /*
             * Query Parameters
             */
            if (parameter.isAnnotationPresent(
                    ActionQueryParam.class
            )) {

                String paramName =
                        parameter
                                .getAnnotation(
                                        ActionQueryParam.class
                                )
                                .value();

                String value =
                        req.getParameter(paramName);

                args[i] =
                        convert(
                                value,
                                parameter.getType()
                        );

                continue;
            }

            /*
             * Path Parameters
             */
            if (parameter.isAnnotationPresent(
                    ActionPathParam.class
            )) {

                String paramName =
                        parameter
                                .getAnnotation(
                                        ActionPathParam.class
                                )
                                .value();

                String value =
                        pathVariables.get(paramName);

                args[i] =
                        convert(
                                value,
                                parameter.getType()
                        );

                continue;
            }

            /*
             * Request Body
             */
            if (parameter.isAnnotationPresent(
                    ActionRequestBody.class
            )) {

                String contentType =
                        req.getContentType();

                /*
                 * JSON REQUEST
                 */
                if (contentType != null
                        && contentType.contains("application/json")) {

                    args[i] =
                            mapper.readValue(
                                    req.getInputStream(),
                                    parameter.getType()
                            );

                } else {

                    /*
                     * FORM REQUEST
                     */
                    args[i] =
                            bindForm(
                                    req,
                                    parameter.getType()
                            );
                }
            }
        }

        return args;
    }

    /*
     * FORM BINDING
     */
    private static Object bindForm(
            HttpServletRequest req,
            Class<?> clazz
    ) throws Exception {

        Object object =
                clazz.getDeclaredConstructor()
                        .newInstance();

        for (Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);

            String value =
                    req.getParameter(
                            field.getName()
                    );

            if (value == null
                    || value.isBlank()) {

                continue;
            }

            field.set(
                    object,
                    convert(value, field.getType())
            );
        }

        return object;
    }

    /*
     * TYPE CONVERSION
     */
    private static Object convert(
            String value,
            Class<?> type
    ) {

        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return value;
        }

        if (type == Long.class
                || type == long.class) {

            return Long.parseLong(value);
        }

        if (type == Integer.class
                || type == int.class) {

            return Integer.parseInt(value);
        }

        if (type == Double.class
                || type == double.class) {

            return Double.parseDouble(value);
        }

        if (type == Boolean.class
                || type == boolean.class) {

            return Boolean.parseBoolean(value);
        }

        return value;
    }
}