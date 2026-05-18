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

    private final AppPage appPage =
            new AppPage();

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String path =
                req.getPathInfo();

        if (
                path == null
                        ||
                        path.equals("/")
        ) {

            resp.sendRedirect(
                    req.getContextPath() + "/"
            );

            return;
        }

        String[] parts =
                path.substring(1).split("/");

        if (parts.length < 2) {

            resp.getWriter()
                    .write("Invalid route");

            return;
        }

        String controllerName =
                parts[0];

        String methodName =
                parts[1];

        try {

            Class<?> controllerClass =
                    findController(controllerName);

            if (controllerClass == null) {

                resp.getWriter()
                        .write("Controller not found");

                return;
            }

            /*
             * SECURITY
             */
            boolean isPublic =
                    controllerClass.isAnnotationPresent(
                            PublicRoute.class
                    );

            if (!isPublic) {

                HttpSession session =
                        req.getSession(false);

                if (
                        session == null
                                ||
                                session.getAttribute(
                                        "loggedInUser"
                                ) == null
                ) {

                    resp.sendRedirect(
                            req.getContextPath()
                                    + "/app/auth/login"
                    );

                    return;
                }
            }

            /*
             * CONTROLLER INSTANCE
             */
            Object controller =
                    CDI.current()
                            .select(controllerClass)
                            .get();

            /*
             * TARGET METHOD
             */
            Method targetMethod =
                    findTargetMethod(
                            controllerClass,
                            req.getMethod(),
                            methodName
                    );

            if (targetMethod == null) {

                resp.getWriter()
                        .write("Method not found");

                return;
            }

            /*
             * EXECUTE ACTION
             */
            Object result;

            if (targetMethod.getParameterCount() == 0) {

                result =
                        targetMethod.invoke(
                                controller
                        );

            } else {

                Class<?> parameterType =
                        targetMethod
                                .getParameterTypes()[0];

                /*
                 * HTTP REQUEST
                 */
                if (
                        parameterType.equals(
                                HttpServletRequest.class
                        )
                ) {

                    result =
                            targetMethod.invoke(
                                    controller,
                                    req
                            );
                }

                /*
                 * HTTP SESSION
                 */
                else if (
                        parameterType.equals(
                                HttpSession.class
                        )
                ) {

                    result =
                            targetMethod.invoke(
                                    controller,
                                    req.getSession()
                            );
                }

                /*
                 * UNKNOWN PARAMETER
                 */
                else {

                    throw new RuntimeException(
                            "Unsupported parameter type: "
                                    + parameterType.getName()
                    );
                }
            }

            /*
             * RESPONSE TYPE
             */
            if (!(result instanceof ActionResponse response)) {

                resp.getWriter()
                        .write("Invalid response");

                return;
            }

            /*
             * REDIRECT
             */
            if (
                    response.getRedirectResponse()
                            != null
            ) {

                RedirectResponse redirect =
                        response.getRedirectResponse();

                resp.sendRedirect(
                        redirect.getUrl()
                );

                return;
            }

            /*
             * PAGE CONTENT
             */
            String content = "";

            /*
             * TABLE RESPONSE
             */
            if (
                    response.getResponseClazz() != null
                            &&
                            response.getResponseDataList() != null
            ) {

                content =
                        appPage
                                .getFramework()
                                .table(
                                        response.getResponseClazz(),
                                        response.getResponseDataList()
                                );
            }

            /*
             * TEXT RESPONSE
             */
            else if (
                    response.getResponseText()
                            != null
            ) {

                content =
                        response.getResponseText();
            }

            /*
             * DASHBOARD RESPONSE
             */
            else if (
                    response.getDashboardResponse()
                            != null
            ) {

                content =
                        response
                                .getDashboardResponse()
                                .render();
            }

            /*
             * AUTH PAGE
             */
            boolean authPage =
                    controllerName.equals("auth");

            if (authPage) {

                resp.setContentType(
                        "text/html"
                );

                resp.getWriter()
                        .write(content);

                return;
            }

            /*
             * NORMAL APP PAGE
             */
            appPage.display(
                    req,
                    resp,
                    content
            );

        } catch (Exception e) {

            throw new ServletException(e);
        }
    }

    /*
     * FIND CONTROLLER
     */
    private Class<?> findController(
            String controllerName
    ) {

        String packageName =
                "app.action";

        try {

            Class<?>[] classes = {

                    Class.forName(
                            packageName + ".AuthAction"
                    ),

                    Class.forName(
                            packageName + ".DashboardAction"
                    ),

                    Class.forName(
                            packageName + ".StudentAction"
                    ),

                    Class.forName(
                            packageName + ".CourseAction"
                    ),

                    Class.forName(
                            packageName + ".DeferralAction"
                    ),

                    Class.forName(
                            packageName + ".AuditTrailAction"
                    ),

                    Class.forName(
                            packageName + ".StudentPortalAction"
                    ),

                    Class.forName(
                            packageName + ".HomeAction"
                    )
            };

            for (Class<?> clazz : classes) {

                if (
                        clazz.isAnnotationPresent(
                                Action.class
                        )
                ) {

                    Action action =
                            clazz.getAnnotation(
                                    Action.class
                            );

                    if (
                            action.value()
                                    .equals(controllerName)
                    ) {

                        return clazz;
                    }
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    /*
     * FIND TARGET METHOD
     */
    private Method findTargetMethod(
            Class<?> controllerClass,
            String httpMethod,
            String methodName
    ) {

        for (
                Method method :
                controllerClass.getDeclaredMethods()
        ) {

            /*
             * GET
             */
            if (
                    httpMethod.equalsIgnoreCase("GET")
                            &&
                            method.isAnnotationPresent(
                                    ActionGetMethod.class
                            )
            ) {

                ActionGetMethod annotation =
                        method.getAnnotation(
                                ActionGetMethod.class
                        );

                if (
                        annotation.value()
                                .equals(methodName)
                ) {

                    return method;
                }
            }

            /*
             * POST
             */
            if (
                    httpMethod.equalsIgnoreCase("POST")
                            &&
                            method.isAnnotationPresent(
                                    ActionPostMethod.class
                            )
            ) {

                ActionPostMethod annotation =
                        method.getAnnotation(
                                ActionPostMethod.class
                        );

                if (
                        annotation.value()
                                .equals(methodName)
                ) {

                    return method;
                }
            }
        }

        return null;
    }
}