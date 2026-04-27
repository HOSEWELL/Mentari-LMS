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

    private final Validate<String> courseValidator;
    private final JdbcRepository<Course> courseRepo;

    // constructor for Servlet Container
    public CourseAction() {
        this.courseValidator = null;
        this.courseRepo = null;
    }

    // --- CONSTRUCTOR INJECTION ---
    @Inject
    public CourseAction(@ValidatorQualifier(ValidatorQualifier.ValidationChoice.COURSE) Validate<String> courseValidator,
                        JdbcRepository<Course> courseRepo )
    {
        this.courseValidator = courseValidator;
        this.courseRepo = courseRepo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Course> courses = courseRepo.findAll();
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/WEB-INF/views/admin/courses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Course course = super.serializeForm(req.getParameterMap(), courseValidator);

        if (course != null) {
            courseRepo.save(course);
            resp.sendRedirect(req.getContextPath() + "/admin/courses?success=true");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/courses?error=invalid_name");
        }
    }
}