package app.model;

import app.framework.annotation.MentariForm;
import app.framework.annotation.MentariFormField;
import app.framework.annotation.MentariTableColumn;
import app.framework.annotation.MentariTableView;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")

@MentariTableView(
        title = "Courses",
        addAction = "/app/courses/save"
)

@MentariForm(
        actionUrl = "/app/courses/save",
        label = "Add Course"
)
public class Course extends BaseEntity {

    @MentariTableColumn(
            label = "Course Name"
    )
    @MentariFormField(
            label = "Course Name",
            placeholder = "Enter course name"
    )
    @Column(name = "name")
    private String name;

    @MentariTableColumn(
            label = "Course Code"
    )
    @MentariFormField(
            label = "Course Code",
            placeholder = "Enter course code"
    )
    @Column(name = "code")
    private String code;

    @MentariTableColumn(
            label = "Description"
    )
    @MentariFormField(
            label = "Description",
            placeholder = "Enter course description"
    )
    @Column(columnDefinition = "TEXT")
    private String description;

    public Course() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}