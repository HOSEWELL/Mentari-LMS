package app.soap;

import app.model.User;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWrapper implements Serializable {

    private List<User> user;

    public UserWrapper() {}

    public UserWrapper(List<User> user) {

        this.user = user;
    }

    public List<User> getUser() {

        return user;
    }

    public void setUser(List<User> user) {

        this.user = user;
    }
}