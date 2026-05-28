package app.filter;

import app.utility.security.jwt.JwtUtil;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.contains("auth/login")) return;

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abortRequest(requestContext, "Missing or invalid Authorization header");
            return;
        }

        try {
            JwtUtil.validateToken(authHeader.replace("Bearer ", ""));
            System.out.println("JWT TOKEN VALID");
        } catch (Exception e) {
            abortRequest(requestContext, "Invalid or expired token");
        }
    }

    private void abortRequest(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
    }
}