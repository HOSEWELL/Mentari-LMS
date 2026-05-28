package app.filter;

import app.framework.annotation.AdminOnly;
import app.utility.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@AdminOnly
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AdminFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing token").build());
            return;
        }

        try {
            Claims claims = JwtUtil.validateToken(authHeader.replace("Bearer ", ""));
            String role = claims.get("role", String.class);
            if (
                    role == null || !role.equalsIgnoreCase("ADMIN")
            ) {

                requestContext.abortWith(
                        Response.status(
                                        Response.Status.FORBIDDEN  )
                                .entity("ADMIN access only").build()
                );
            }
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
        }
    }
}