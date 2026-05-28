package app.rest;

import app.bean.UserService;
import app.model.User;
import app.utility.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Tag(name = "Auth", description = "Auth management endpoints")

public class AuthRestApi {

    @EJB
    private UserService userService;

    @Path("/login") @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        if (user == null)
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ResponseStatus(false, "Invalid username or password"))
                .build();
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        return Response.ok(new LoginResponse(token, user.getRole(), user.getUsername())).build();
    }
}