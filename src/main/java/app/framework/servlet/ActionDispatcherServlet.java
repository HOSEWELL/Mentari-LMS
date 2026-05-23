package app.framework.servlet;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.PublicRoute;
import app.framework.rendering.AppPage;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/app/*")
public class ActionDispatcherServlet extends HttpServlet {

    private final AppPage appPage = new AppPage();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String[] parts = path.substring(1).split("/");

        if (parts.length < 2) {
            resp.getWriter().write("Invalid route");
            return;
        }

        String controllerName = parts[0];
        String methodName = parts[1];

        try {
            Class<?> controllerClass = findController(controllerName);

            if (controllerClass == null) {
                resp.getWriter().write("Controller not found");
                return;
            }

            if (!controllerClass.isAnnotationPresent(PublicRoute.class)) {
                HttpSession session = req.getSession(false);
                if (session == null || session.getAttribute("loggedInUser") == null) {
                    resp.sendRedirect(req.getContextPath() + "/app/auth/login");
                    return;
                }
            }

            Object controller = CDI.current().select(controllerClass).get();
            Method targetMethod = findTargetMethod(controllerClass, req.getMethod(), methodName);

            if (targetMethod == null) {
                resp.getWriter().write("Method not found");
                return;
            }

            int parameterCount = targetMethod.getParameterCount();
            Object result;

            if (parameterCount == 0) {
                result = targetMethod.invoke(controller);
            } else if (parameterCount == 1) {
                Class<?> parameterType = targetMethod.getParameterTypes()[0];
                if (parameterType.equals(HttpServletRequest.class)) {
                    result = targetMethod.invoke(controller, req);
                } else if (parameterType.equals(HttpSession.class)) {
                    result = targetMethod.invoke(controller, req.getSession());
                } else {
                    Object model = parameterType.getDeclaredConstructor().newInstance();
                    bindRequestData(model, req);
                    result = targetMethod.invoke(controller, model);
                }
            } else if (parameterCount == 2) {
                Object[] args = new Object[2];
                Class<?> firstParam = targetMethod.getParameterTypes()[0];
                Class<?> secondParam = targetMethod.getParameterTypes()[1];

                if (firstParam.equals(HttpServletRequest.class)) { args[0] = req; }
                else if (firstParam.equals(HttpSession.class)) { args[0] = req.getSession(); }
                else { Object model = firstParam.getDeclaredConstructor().newInstance(); bindRequestData(model, req); args[0] = model; }

                if (secondParam.equals(HttpServletRequest.class)) { args[1] = req; }
                else if (secondParam.equals(HttpSession.class)) { args[1] = req.getSession(); }
                else { Object model = secondParam.getDeclaredConstructor().newInstance(); bindRequestData(model, req); args[1] = model; }

                result = targetMethod.invoke(controller, args);
            } else {
                throw new RuntimeException("Unsupported parameter count: " + parameterCount);
            }

            if (result instanceof RedirectResponse redirectResponse) {
                resp.sendRedirect(redirectResponse.getUrl());
                return;
            }

            if (!(result instanceof ActionResponse)) {
                resp.getWriter().write("Invalid response");
                return;
            }

            ActionResponse response = (ActionResponse) result;

            if (response.getRedirectResponse() != null) {
                resp.sendRedirect(response.getRedirectResponse().getUrl());
                return;
            }

            String content = "";

            if (response.getResponseClazz() != null && response.getResponseDataList() != null) {
                String role = (String) req.getSession().getAttribute("role");
                content = appPage.getFramework().table(response.getResponseClazz(), response.getResponseDataList(), role);
            } else if (response.getResponseText() != null) {
                content = response.getResponseText();
            } else if (response.getDashboardResponse() != null) {
                content = response.getDashboardResponse().render();
            }

            if (controllerName.equals("auth")) {
                resp.setContentType("text/html");
                resp.getWriter().write(content);
                return;
            }

            appPage.display(req, resp, content);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void bindRequestData(Object model, HttpServletRequest req) {
        req.getParameterMap().forEach((key, value) -> {
            try {
                var field = model.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType.equals(String.class)) { field.set(model, value[0]); }
                else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) { field.set(model, Long.parseLong(value[0])); }
                else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) { field.set(model, Integer.parseInt(value[0])); }
                else if (fieldType.getName().equals("java.time.LocalDate")) { field.set(model, java.time.LocalDate.parse(value[0])); }
            } catch (Exception ignored) {}
        });
    }

    private Class<?> findController(String controllerName) {
        String packageName = "app.action";
        try {
            Class<?>[] classes = {
                    Class.forName(packageName + ".AuthAction"),
                    Class.forName(packageName + ".DashboardAction"),
                    Class.forName(packageName + ".StudentAction"),
                    Class.forName(packageName + ".CourseAction"),
                    Class.forName(packageName + ".DeferralAction"),
                    Class.forName(packageName + ".AuditTrailAction"),
                    Class.forName(packageName + ".StudentPortalAction"),
                    Class.forName(packageName + ".AccountAction"),
                    Class.forName(packageName + ".HomeAction")
            };
            for (Class<?> clazz : classes) {
                if (!clazz.isAnnotationPresent(Action.class)) continue;
                Action action = clazz.getAnnotation(Action.class);
                if (action.value().equals(controllerName)) return clazz;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private Method findTargetMethod(Class<?> controllerClass, String httpMethod, String methodName) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (httpMethod.equalsIgnoreCase("GET") && method.isAnnotationPresent(ActionGetMethod.class)) {
                String route = method.getAnnotation(ActionGetMethod.class).value();
                if (route.equals(methodName) || route.startsWith(methodName + "/{")) return method;
            }
            if (httpMethod.equalsIgnoreCase("POST") && method.isAnnotationPresent(ActionPostMethod.class)) {
                String route = method.getAnnotation(ActionPostMethod.class).value();
                if (route.equals(methodName) || route.startsWith(methodName + "/{")) return method;
            }
        }
        return null;
    }
}