package app.model;

import app.framework.MentariTable;
import app.framework.MentariColumn;

@MentariTable(name = "users")
public class User {
    @MentariColumn(name = "id", primaryKey = true)
    private Long id;

    @MentariColumn(name = "username")
    private String username;

    @MentariColumn(name = "password")
    private String password;

    @MentariColumn(name = "role")
    private String role;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}