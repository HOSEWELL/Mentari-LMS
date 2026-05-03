package app.bean;

import app.dao.UserDao;
import app.model.User;
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

    public User authenticate(String username, String password) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}