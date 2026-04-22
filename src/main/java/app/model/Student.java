package app.model;

import app.framework.MentariTable;
import app.framework.MentariColumn;

@MentariTable(name = "students")
public class Student {
    @MentariColumn(name = "id", primaryKey = true)
    private Long id;

    @MentariColumn(name = "full_name")
    private String fullName;

    @MentariColumn(name = "email")
    private String email;

    @MentariColumn(name = "registration_number")
    private String regNumber;

    public Student() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRegNumber() { return regNumber; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }
}