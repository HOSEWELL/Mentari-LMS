package app.model;

import app.framework.annotation.MentariForm;
import app.framework.annotation.MentariFormField;
import app.framework.annotation.MentariRelationshipLabel;
import app.framework.annotation.MentariSelect;
import app.framework.annotation.MentariTableColumn;
import app.framework.annotation.MentariTableView;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollments")

@MentariTableView(
        title = "Enrollments",
        addAction = "/Mentari/app/enrollments/list"
)

@MentariForm(
        actionUrl = "/Mentari/app/enrollments/enroll",
        label = "Enroll Student"
)
public class Enrollment extends BaseEntity {

    @MentariTableColumn(
            label = "Student"
    )
    @MentariRelationshipLabel("fullName")
    @MentariFormField(
            label = "Student"
    )
    @MentariSelect(
            entity = Student.class,
            labelField = "fullName"
    )
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @MentariTableColumn(
            label = "Course"
    )
    @MentariRelationshipLabel("name")
    @MentariFormField(
            label = "Course"
    )
    @MentariSelect(
            entity = Course.class,
            labelField = "name"
    )
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Enrollment() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}