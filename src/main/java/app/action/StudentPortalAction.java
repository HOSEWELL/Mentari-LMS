package app.action;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ProtectedRoute;
import app.framework.response.ActionResponse;

@ProtectedRoute(roles = {"STUDENT"})
@Action(value = "student-portal", label = "Student Portal", roles = {"STUDENT"}, showLink = true)
public class StudentPortalAction {

    @ActionGetMethod("index")
    public ActionResponse index() {
        return new ActionResponse("""
<div class='dashboard-hero' style='display:flex; justify-content:space-between; align-items:center; padding:40px; border-radius:24px; background:linear-gradient(135deg,#2563eb,#1d4ed8); color:white; margin-bottom:30px;'>
<div>
<p style='opacity:0.9; margin-bottom:10px; font-size:14px; letter-spacing:1px;'>MENTARI LMS</p>
<h1 class='page-title' style='font-size:42px; margin-bottom:15px; color:white;'>Welcome Back </h1>
<p class='hero-subtitle' style='max-width:600px; line-height:1.7; color:rgba(255,255,255,0.9); font-size:16px;'>Access your learning portal, manage your deferrals, track your academic activities and stay updated with everything happening in Mentari LMS.</p>
<div style='display:flex; gap:15px; margin-top:25px; flex-wrap:wrap;'>
<div style='background:rgba(255,255,255,0.12); padding:12px 18px; border-radius:14px; backdrop-filter:blur(8px);'><i class='fas fa-circle-check'></i> Academic Status: Active</div>
<div style='background:rgba(255,255,255,0.12); padding:12px 18px; border-radius:14px; backdrop-filter:blur(8px);'><i class='fas fa-wifi'></i> Portal Online</div>
</div>
</div>
<div style='width:110px; height:110px; border-radius:24px; display:flex; justify-content:center; align-items:center; background:rgba(255,255,255,0.15); font-size:48px;'><i class='fas fa-user-graduate'></i></div>
</div>

<div style='display:grid; grid-template-columns:repeat(auto-fit,minmax(260px,1fr)); gap:24px; margin-bottom:30px;'>

<div class='card' style='padding:28px; border-radius:22px; border-left:6px solid #2563eb;'>
<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px;'>
<div><p style='color:#6b7280; font-size:14px; margin-bottom:8px;'>Student Role</p><h2 style='font-size:30px;'>Student</h2></div>
<div style='width:60px; height:60px; border-radius:18px; background:#2563eb; color:white; display:flex; justify-content:center; align-items:center; font-size:24px;'><i class='fas fa-book-open'></i></div>
</div>
<p style='color:#6b7280; line-height:1.6;'>Access learning services, manage your profile and submit academic requests.</p>
</div>

<div class='card' style='padding:28px; border-radius:22px; border-left:6px solid #16a34a;'>
<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px;'>
<div><p style='color:#6b7280; font-size:14px; margin-bottom:8px;'>Portal Access</p><h2 style='font-size:30px;'>24 / 7</h2></div>
<div style='width:60px; height:60px; border-radius:18px; background:#16a34a; color:white; display:flex; justify-content:center; align-items:center; font-size:24px;'><i class='fas fa-globe'></i></div>
</div>
<p style='color:#6b7280; line-height:1.6;'>Your student portal is always available for academic services and updates.</p>
</div>

<div class='card' style='padding:28px; border-radius:22px; border-left:6px solid #f59e0b;'>
<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px;'>
<div><p style='color:#6b7280; font-size:14px; margin-bottom:8px;'>Quick Access</p><h2 style='font-size:30px;'>Deferrals</h2></div>
<div style='width:60px; height:60px; border-radius:18px; background:#f59e0b; color:white; display:flex; justify-content:center; align-items:center; font-size:24px;'><i class='fas fa-file-signature'></i></div>
</div>
<p style='color:#6b7280; line-height:1.6;'>Submit and monitor academic deferral requests directly from your portal.</p>
</div>

</div>

<div class='card' style='padding:35px; border-radius:24px;'>
<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:25px; flex-wrap:wrap; gap:15px;'>
<div><h2 style='margin-bottom:10px;'>Student Information</h2><p style='color:#6b7280;'>Your academic portal overview and account services.</p></div>
<div style='padding:10px 18px; background:#dcfce7; color:#16a34a; border-radius:999px; font-weight:600;'>Online</div>
</div>
<div style='display:grid; grid-template-columns:repeat(auto-fit,minmax(250px,1fr)); gap:25px;'>
<div style='padding:22px; border-radius:18px; background:#f8fafc;'>
<p style='color:#6b7280; margin-bottom:10px; font-size:14px;'>Services Available</p>
<h3 style='margin-bottom:10px;'>Academic Management</h3>
<p style='color:#6b7280; line-height:1.6;'>Manage requests, portal access and account details easily.</p>
</div>
<div style='padding:22px; border-radius:18px; background:#f8fafc;'>
<p style='color:#6b7280; margin-bottom:10px; font-size:14px;'>Portal Security</p>
<h3 style='margin-bottom:10px;'>Protected Access</h3>
<p style='color:#6b7280; line-height:1.6;'>Your account is secured and monitored through Mentari LMS.</p>
</div>
<div style='padding:22px; border-radius:18px; background:#f8fafc;'>
<p style='color:#6b7280; margin-bottom:10px; font-size:14px;'>Current Session</p>
<h3 style='margin-bottom:10px; color:#16a34a;'>Active Session</h3>
<p style='color:#6b7280; line-height:1.6;'>You are currently connected and using the student portal.</p>
</div>
</div>
</div>
""");
    }
}