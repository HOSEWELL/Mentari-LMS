package app.action;

import app.bean.StudentService;
import app.framework.annotation.*;
import app.framework.response.ActionResponse;
import app.model.Student;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Action(value = "students", label = "Students", roles = {"ADMIN"}, showLink = true)
@ProtectedRoute(roles = {"ADMIN"})
public class StudentAction {

    @EJB
    private StudentService studentService;

    @ActionGetMethod("list")
    public ActionResponse list() {
        return new ActionResponse(Student.class, studentService.findAll());
    }

    @ActionPostMethod("save")
    public ActionResponse save(@ActionRequestBody Student student) {
        studentService.enrollStudent(student);
        return new ActionResponse(Student.class, studentService.findAll());
    }
}