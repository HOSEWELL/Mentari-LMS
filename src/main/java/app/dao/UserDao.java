package app.dao;

import app.model.User;
import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDao extends GenericDao<User, Long> {

    public User findByUsername(String username) {

        try {

            return getEm()
                    .createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    )
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }
}