package app.soap;

import app.model.Course;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "courses")
@XmlAccessorType(XmlAccessType.FIELD)
public class CourseWrapper implements Serializable {

    private List<Course> course;

    public CourseWrapper() {}

    public CourseWrapper(List<Course> course) {

        this.course = course;
    }

    public List<Course> getCourse() {

        return course;
    }

    public void setCourse(List<Course> course) {

        this.course = course;
    }
}