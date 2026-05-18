package app.model;

import app.framework.annotation.MentariForm;
import app.framework.annotation.MentariFormField;
import app.framework.annotation.MentariTableColumn;
import app.framework.annotation.MentariTableView;

import jakarta.persistence.*;

@Entity
@Table(name = "students")

@MentariTableView(
        title = "Students",
        addAction = "/app/students/list"
)

@MentariForm(
        actionUrl = "/app/students/save",
        label = "Register Student"
)
public class Student extends BaseEntity {

    @MentariTableColumn(
            label = "Student Name"
    )
    @MentariFormField(
            label = "Full Name",
            placeholder = "Enter full name"
    )
    @Column(name = "fullName")
    private String fullName;

    @MentariTableColumn(
            label = "Email"
    )
    @MentariFormField(
            label = "Student Email",
            placeholder = "Enter email",
            type = "email"
    )
    @Column(unique = true)
    private String email;

    @MentariTableColumn(
            label = "Registration Number"
    )
    @MentariFormField(
            label = "Registration Number",
            placeholder = "Enter registration number"
    )
    @Column(name = "regNumber")
    private String regNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Student() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}