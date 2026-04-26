package app.action;

import app.framework.BaseAction;
import app.model.Course;
import app.repository.JdbcRepository;
import app.util.validation.Validate;
import app.util.validation.ValidatorQualifier;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/courses")
public class CourseAction extends BaseAction<Course> {

    @Inject
    @ValidatorQualifier(ValidatorQualifier.ValidationChoice.COURSE)
    private Validate<String> courseValidator;

    private final JdbcRepository<Course> courseRepo = new JdbcRepository<>(Course.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Course> courses = courseRepo.findAll();
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/WEB-INF/views/admin/courses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Pass the injected validator to the base framework
        Course course = super.serializeForm(req.getParameterMap(), courseValidator);

        if (course != null) {
            courseRepo.save(course);
            System.out.println("MENTARI >>> New Course Added: " + course.getName());
            resp.sendRedirect(req.getContextPath() + "/admin/courses?success=true");
        } else {
            // Validation failed (name too short)
            resp.sendRedirect(req.getContextPath() + "/admin/courses?error=invalid_name");
        }
    }
}