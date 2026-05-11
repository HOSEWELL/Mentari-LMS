package app.action;

import app.bean.DeferralService;
import app.model.Deferral;
import app.model.Student;
import app.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet({"/student/defer", "/admin/deferrals"})
public class DeferralAction extends HttpServlet {

    @EJB
    private DeferralService deferralService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        req.setAttribute("loggedInUser", loggedInUser);

        String uri = req.getRequestURI();

        if (uri.contains("/admin/deferrals")) {
            // Admin sees all requests
            List<Deferral> deferrals = deferralService.findAll();
            req.setAttribute("deferrals", deferrals);
            req.getRequestDispatcher("/WEB-INF/views/admin/deferrals.jsp")
                    .forward(req, resp);

        } else {
            // Student sees only their own
            Student student = (Student) session.getAttribute("loggedInStudent");
            if (student != null) {
                List<Deferral> myDeferrals = deferralService.findByStudentId(student.getId());
                req.setAttribute("myDeferrals", myDeferrals);
            }
            req.getRequestDispatcher("/WEB-INF/views/student/defer.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("/admin/deferrals")) {
            // Admin approving or rejecting
            Long id = Long.parseLong(req.getParameter("id"));
            String action = req.getParameter("action");
            String studentName = req.getParameter("studentName");

            if ("approve".equals(action)) {
                deferralService.approve(id, studentName);
            } else if ("reject".equals(action)) {
                deferralService.reject(id, studentName);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/deferrals?success=true");

        } else {
            // Student submitting a new request
            HttpSession session = req.getSession(false);
            Student student = (Student) session.getAttribute("loggedInStudent");

            Deferral deferral = new Deferral();
            deferral.setStartDate(req.getParameter("startDate"));
            deferral.setEndDate(req.getParameter("endDate"));
            deferral.setReason(req.getParameter("reason"));

            if (student != null) {
                deferral.setStudentId(student.getId());
                deferral.setStudentName(student.getFullName());
            }

            deferralService.submitRequest(deferral);
            resp.sendRedirect(req.getContextPath() + "/student/defer?success=true");
        }
    }
}