package app.model;

import app.framework.annotation.MentariForm;
import app.framework.annotation.MentariFormField;
import app.framework.annotation.MentariTableColumn;
import app.framework.annotation.MentariTableView;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

@MentariTableView(
        title = "Users"
)

@MentariForm(
        actionUrl = "/app/users/save",
        label = "Save User"
)
public class User extends BaseEntity {

    @MentariTableColumn(
            label = "Username"
    )
    @MentariFormField(
            label = "Username",
            placeholder = "Enter username",
            hidden = false
    )
    @Column(nullable = false, unique = true)
    private String username;

    @MentariTableColumn(
            label = "Password",
            hidden = true
    )
    @MentariFormField(
            label = "Password",
            placeholder = "Enter password",
            type = "password",
            hidden = false
    )
    @Column(nullable = false)
    private String password;

    @MentariTableColumn(
            label = "Role"
    )
    @MentariFormField(
            label = "Role",
            placeholder = "ADMIN or STUDENT",
            hidden = true
    )
    @Column(nullable = false)
    private String role;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}