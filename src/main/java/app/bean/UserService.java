package app.bean;

import app.dao.UserDao;
import app.model.User;
import app.utility.security.PasswordUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UserService {

    @Inject
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User authenticate( String username,String password) {

        User user =
                userDao.findByUsername(username);

        if (user == null) {
            return null;
        }

        boolean valid =
                PasswordUtil.verifyPassword(
                        password,
                        user.getPassword()
                );

        return valid ? user : null;
    }
}