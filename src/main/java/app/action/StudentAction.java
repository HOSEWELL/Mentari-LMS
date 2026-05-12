package app.action;

import app.bean.StudentService;
import app.framework.BaseAction;
import app.model.Student;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/students")
public class StudentAction extends BaseAction<Student> {

    @EJB
    private StudentService studentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Student> students = studentService.findAll();
        req.setAttribute("students", students);
        req.getRequestDispatcher("/WEB-INF/views/admin/students.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Student student = super.serializeForm(req.getParameterMap());

        if (studentService.enrollStudent(student)) {
            resp.sendRedirect(req.getContextPath()
                    + "/admin/students?success=true");
        } else {
            resp.sendRedirect(req.getContextPath()
                    + "/admin/students?error=blocked_name");
        }
    }
}