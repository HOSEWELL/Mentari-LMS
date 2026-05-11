package app.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/student/*", "/admin-dashboard", "/student-portal"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // FIX: Changed "user" to "loggedInUser" to match your LoginAction.java
        boolean loggedIn = (session != null && session.getAttribute("loggedInUser") != null);

        String loginURI = req.getContextPath() + "/login";

        String requestURI = req.getRequestURI();

        // Check if the user is logged in or requesting the login page
        if (loggedIn || requestURI.equals(loginURI) || requestURI.endsWith("login.jsp")) {
            chain.doFilter(request, response);
        } else {
            // Not logged in, redirect to login
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}