package app.soap;

import app.model.Student;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "students")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentWrapper implements Serializable {

    private List<Student> student;

    public StudentWrapper() {}

    public StudentWrapper(List<Student> student) {

        this.student = student;
    }

    public List<Student> getStudent() {

        return student;
    }

    public void setStudent(List<Student> student) {

        this.student = student;
    }
}