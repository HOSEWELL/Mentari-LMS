package app.action;

import app.bean.CourseService;
import app.framework.BaseAction;
import app.model.Course;
import app.utility.validation.Validate;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/courses")
public class CourseAction extends BaseAction<Course> {

    @EJB
    private CourseService courseService;

    @Inject
    @Named("Course")
    private Validate<String> courseValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Course> courses = courseService.findAll();
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/WEB-INF/views/admin/courses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Course course = super.serializeForm(req.getParameterMap(), courseValidator);
        if (course != null) {
            courseService.save(course);
            resp.sendRedirect(req.getContextPath() + "/admin/courses?success=true");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/courses?error=invalid_name");
        }
    }
}