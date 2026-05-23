package app.soap;

import app.bean.StudentService;
import app.model.Student;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService
public class StudentSoapApi {

    @EJB
    private StudentService studentService;

    @WebMethod
    public boolean enrollStudent(
            @WebParam(name = "student") Student student
    ) {

        return studentService.enrollStudent(student);
    }

    @WebMethod
    public Student update(
            @WebParam(name = "student") Student student
    ) {

        return studentService.update(student);
    }

    @WebMethod
    @WebResult(name = "students")
    public StudentWrapper list() {

        return new StudentWrapper(
                studentService.findAll()
        );
    }
}