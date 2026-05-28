package app.rest;

import app.bean.UserService;
import app.framework.annotation.AdminOnly;
import app.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/user")
@Tag(name = "User", description = "User management endpoints")
public class UserRestApi {

    @EJB
    private UserService userService;

    @Path("/save")
    @AdminOnly
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User save(User user) { return userService.save(user); }

    @Path("/update")
    @PUT
    @AdminOnly
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(User user) {
        userService.update(user);
        return Response.ok(new ResponseStatus(true, "User updated successfully")).build();
    }

    @Path("/list") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() { return userService.findAll(); }
}