package app.action;

import app.bean.AuditTrailBean;
import app.model.AuditTrail;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/audit-trail")
public class AuditTrailAction extends HttpServlet {

    @EJB
    private AuditTrailBean auditTrailBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<AuditTrail> logs = auditTrailBean.findAll();
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/WEB-INF/views/admin/audit_trail.jsp")
                .forward(req, resp);
    }
}