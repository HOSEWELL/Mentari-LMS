package app.soap;

import app.bean.UserService;
import app.model.User;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService
public class UserSoapApi {

    @EJB
    private UserService userService;

    @WebMethod
    public User save(
            @WebParam(name = "user") User user
    ) {

        return userService.save(user);
    }

    @WebMethod
    public User authenticate(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password
    ) {

        return userService.authenticate(
                username,
                password
        );
    }

    @WebMethod
    @WebResult(name = "users")
    public UserWrapper list() {

        return new UserWrapper(
                userService.findAll()
        );
    }
}