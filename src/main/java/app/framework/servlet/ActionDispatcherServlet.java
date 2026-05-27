package app.framework.servlet;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ActionPostMethod;
import app.framework.annotation.PublicRoute;
import app.framework.binding.ActionParamBinder;
import app.framework.rendering.AppPage;
import app.framework.response.ActionResponse;
import app.framework.response.RedirectResponse;
import app.framework.routing.ActionMap;
import app.framework.routing.ActionRegistry;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

@WebServlet("/app/*")
public class ActionDispatcherServlet extends HttpServlet {

    private final AppPage appPage = new AppPage();

    @Override
    public void init() {
        ActionRegistry.scanAndRegister("app.action");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) { resp.sendRedirect(req.getContextPath() + "/"); return; }

        String[] parts = path.substring(1).split("/");

        if (parts.length < 2) { resp.getWriter().write("Invalid route"); return; }

        String controllerName = parts[0];
        String methodName = parts[1];

        try {
            Class<?> controllerClass = null;

            for (Class<?> clazz : ActionRegistry.getRegisteredActions()) {
                Action action = clazz.getAnnotation(Action.class);
                if (action.value().equals(controllerName)) { controllerClass = clazz; break; }
            }

            if (controllerClass == null) { resp.getWriter().write("Controller not found"); return; }

            if (!controllerClass.isAnnotationPresent(PublicRoute.class)) {
                HttpSession session = req.getSession(false);
                if (session == null || session.getAttribute("loggedInUser") == null) {
                    resp.sendRedirect(req.getContextPath() + "/app/auth/login");
                    return;
                }
            }

            Object controller = CDI.current().select(controllerClass).get();
            Method targetMethod = findTargetMethod(controllerClass, req.getMethod(), methodName);

            if (targetMethod == null) { resp.getWriter().write("Method not found"); return; }

            ActionMap actionMap = new ActionMap(controllerClass, targetMethod, req.getMethod(), path);
            Object[] args = ActionParamBinder.bind(actionMap, req, resp, new HashMap<>());
            Object result = targetMethod.invoke(controller, args);

            if (result instanceof RedirectResponse redirectResponse) {
                resp.sendRedirect(redirectResponse.getUrl());
                return;
            }

            if (!(result instanceof ActionResponse)) { resp.getWriter().write("Invalid response"); return; }

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