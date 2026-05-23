package app.framework.rendering;

import app.framework.annotation.Action;
import app.framework.routing.ActionRegistry;
import app.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ApplicationScoped
public class AppPage {

    private final MentariFramework framework = new MentariFramework();

    public MentariFramework getFramework() { return framework; }

    public void display(HttpServletRequest req, HttpServletResponse resp, String pageContent) throws IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        User loggedInUser = (User) req.getSession().getAttribute("loggedInUser");
        String currentRole = loggedInUser != null ? loggedInUser.getRole() : "GUEST";

        out.println("""
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mentari LMS</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
*{ margin:0; padding:0; box-sizing:border-box; font-family:'Segoe UI',sans-serif; }
body{ background:#f3f4f6; display:flex; color:#111827; }
.sidebar{ width:260px; height:100vh; background:#0f172a; position:fixed; left:0; top:0; padding:24px 18px; overflow-y:auto; border-right:1px solid rgba(255,255,255,0.05); }
.logo{ font-size:26px; font-weight:700; color:#60a5fa; margin-bottom:35px; display:flex; align-items:center; gap:12px; }
.menu{ display:flex; flex-direction:column; gap:10px; }
.menu a{ text-decoration:none; color:#cbd5e1; padding:14px 16px; border-radius:12px; transition:0.2s; display:flex; align-items:center; gap:12px; font-size:15px; font-weight:500; }
.menu a:hover{ background:#1e293b; color:white; transform:translateX(4px); }
.active-link{ background:#2563eb !important; color:white !important; box-shadow:0 8px 20px rgba(37,99,235,0.25); }
.active-link i{ color:white !important; }
.main{ margin-left:260px; padding:35px; width:calc(100% - 260px); min-height:100vh; }
.topbar{ display:flex; justify-content:space-between; align-items:center; margin-bottom:30px; }
.user-info{ font-size:14px; color:#6b7280; }
.card{ background:white; padding:30px; border-radius:18px; box-shadow:0 4px 20px rgba(0,0,0,0.06); margin-bottom:25px; }
.page-title{ font-size:34px; font-weight:700; margin-bottom:25px; color:#111827; }
.btn{ background:#2563eb; color:white; border:none; padding:12px 22px; border-radius:10px; cursor:pointer; font-weight:600; text-decoration:none; display:inline-block; transition:0.2s; }
.btn:hover{ background:#1d4ed8; }
.btn-danger{ background:#dc2626; }
.btn-danger:hover{ background:#b91c1c; }
table{ width:100%; border-collapse:collapse; margin-top:20px; background:white; overflow:hidden; border-radius:12px; }
thead{ background:#eff6ff; }
th{ padding:16px; text-align:left; font-size:14px; font-weight:700; color:#1e3a8a; border-bottom:1px solid #dbeafe; }
td{ padding:16px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#374151; }
tr:hover{ background:#f9fafb; }
.form-group{ margin-bottom:20px; }
.form-group label{ display:block; margin-bottom:8px; font-weight:600; font-size:14px; color:#374151; }
input, select, textarea{ width:100%; padding:14px; border:1px solid #d1d5db; border-radius:10px; font-size:14px; background:white; }
input:focus, select:focus, textarea:focus{ outline:none; border-color:#2563eb; box-shadow:0 0 0 3px rgba(37,99,235,0.1); }
.dashboard-grid{ display:grid; grid-template-columns:repeat(auto-fit,minmax(250px,1fr)); gap:20px; margin-top:20px; }
.dashboard-card{ background:white; padding:24px; border-radius:18px; box-shadow:0 4px 20px rgba(0,0,0,0.05); transition:0.2s; }
.dashboard-card:hover{ transform:translateY(-6px); box-shadow:0 14px 35px rgba(0,0,0,0.08); }
.dashboard-card-top{ display:flex; justify-content:space-between; align-items:center; }
.dashboard-title{ font-size:14px; color:#6b7280; margin-bottom:8px; }
.dashboard-value{ font-size:34px; font-weight:700; color:#111827; }
.dashboard-icon{ width:58px; height:58px; border-radius:14px; display:flex; align-items:center; justify-content:center; font-size:22px; color:white; }
.table-header{ display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
.floating-btn{ width:52px; height:52px; background:#2563eb; border-radius:50%; display:flex; align-items:center; justify-content:center; color:white; font-size:20px; text-decoration:none; box-shadow:0 10px 20px rgba(37,99,235,0.3); transition:0.2s; }
.floating-btn:hover{ transform:scale(1.08); background:#1d4ed8; }
.approve-btn{ background:#16a34a; color:white; padding:10px 14px; border-radius:8px; text-decoration:none; font-size:13px; font-weight:600; }
.reject-btn{ background:#dc2626; color:white; padding:10px 14px; border-radius:8px; text-decoration:none; font-size:13px; font-weight:600; }
.dashboard-hero{ background:linear-gradient(135deg,#2563eb,#1d4ed8); color:white; padding:40px; border-radius:22px; display:flex; justify-content:space-between; align-items:center; margin-bottom:30px; box-shadow:0 10px 30px rgba(37,99,235,0.25); }
.hero-subtitle{ margin-top:10px; font-size:16px; opacity:0.9; }
.hero-icon{ width:90px; height:90px; border-radius:20px; background:rgba(255,255,255,0.15); display:flex; align-items:center; justify-content:center; font-size:40px; }
@media(max-width:900px){ .sidebar{ width:100%; height:auto; position:relative; } .main{ margin-left:0; width:100%; padding:20px; } .dashboard-grid{ grid-template-columns:1fr; } }
</style>
</head>
<body>
""");

        out.println("<div class='sidebar'><div class='logo'><i class=\"fas fa-graduation-cap\"></i> Mentari LMS</div><div class='menu'>");

        if ("ADMIN".equalsIgnoreCase(currentRole)) {
            out.println("<a class='%s' href='/Mentari/app/dashboard/index'><i class=\"fas fa-chart-line\"></i> Dashboard</a>"
                    .formatted(req.getRequestURI().contains("/dashboard/") ? "active-link" : ""));
        }

        if ("STUDENT".equalsIgnoreCase(currentRole)) {
            out.println("<a class='%s' href='/Mentari/app/student-portal/index'><i class=\"fas fa-house-user\"></i> Dashboard</a>"
                    .formatted(req.getRequestURI().contains("/student-portal/") ? "active-link" : ""));
        }

        for (Class<?> clazz : ActionRegistry.getRegisteredActions()) {
            if (!clazz.isAnnotationPresent(Action.class)) continue;
            Action action = clazz.getAnnotation(Action.class);
            if (!action.showLink()) continue;
            if (action.value().equals("dashboard") || action.value().equals("student-portal")) continue;

            boolean allowed = action.roles().length == 0;
            if (!allowed && loggedInUser != null) {
                for (String role : action.roles()) {
                    if (role.equalsIgnoreCase(loggedInUser.getRole())) { allowed = true; break; }
                }
            }
            if (!allowed) continue;

            String url = switch (action.value()) {
                case "deferrals" -> "ADMIN".equalsIgnoreCase(currentRole) ? "/Mentari/app/deferrals/admin" : "/Mentari/app/deferrals/student";
                default -> "/Mentari/app/" + action.value() + "/list";
            };

            String activeClass = req.getRequestURI().contains("/" + action.value() + "/") ? "active-link" : "";
            out.println("<a class='%s' href='%s'><i class=\"fas fa-table\"></i> %s</a>".formatted(activeClass, url, action.label()));
        }

        out.println("</div></div>");
        out.println("<div class='main'>");

        if (loggedInUser != null) {
            out.println("""
<div class='topbar'>
<div class='user-info'>Logged in as: <strong>%s</strong> (%s)</div>
<div><a class='btn btn-danger' href='/Mentari/app/auth/logout'>Logout</a></div>
</div>
""".formatted(loggedInUser.getUsername(), loggedInUser.getRole()));
        }

        out.println(pageContent);
        out.println("</div></body></html>");
    }
}