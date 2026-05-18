package app.action;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.PublicRoute;

import app.framework.response.ActionResponse;

@PublicRoute

@Action(
        value = "",
        showLink = false
)
public class HomeAction {

    @ActionGetMethod("")
    public ActionResponse index() {

        return new ActionResponse("""
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Mentari LMS</title>

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

<style>

*{
margin:0;
padding:0;
box-sizing:border-box;
font-family:Segoe UI,sans-serif;
}

body{
background:#f4f7fb;
}

/*
NAVBAR
*/
.navbar{
display:flex;
justify-content:space-between;
align-items:center;
padding:20px 60px;
background:white;
box-shadow:0 2px 10px rgba(0,0,0,0.05);
}

.logo{
font-size:24px;
font-weight:bold;
color:#2563eb;
}

.nav-links{
display:flex;
gap:20px;
align-items:center;
}

.login-btn{
background:#2563eb;
color:white;
padding:12px 22px;
border-radius:10px;
text-decoration:none;
font-weight:600;
transition:0.2s;
}

.login-btn:hover{
background:#1d4ed8;
}

/*
HERO
*/
.hero{
display:flex;
justify-content:center;
align-items:center;
height:85vh;
padding:40px;
}

.hero-content{
max-width:700px;
text-align:center;
}

.hero-content h1{
font-size:58px;
margin-bottom:20px;
color:#111827;
}

.hero-content p{
font-size:20px;
color:#6b7280;
margin-bottom:35px;
line-height:1.7;
}

.hero-btn{
display:inline-block;
padding:16px 30px;
background:#2563eb;
color:white;
border-radius:12px;
text-decoration:none;
font-size:18px;
font-weight:bold;
transition:0.2s;
}

.hero-btn:hover{
background:#1d4ed8;
transform:translateY(-2px);
}

.features{
display:grid;
grid-template-columns:repeat(auto-fit,minmax(250px,1fr));
gap:20px;
padding:60px;
}

.feature-card{
background:white;
padding:30px;
border-radius:16px;
box-shadow:0 4px 12px rgba(0,0,0,0.05);
text-align:center;
}

.feature-card i{
font-size:40px;
margin-bottom:20px;
color:#2563eb;
}

.feature-card h3{
margin-bottom:15px;
}

</style>

</head>

<body>

<div class="navbar">

<div class="logo">
<i class="fas fa-graduation-cap"></i>
Mentari LMS
</div>

<div class="nav-links">

<a class="login-btn"
href="/Mentari/app/auth/login">
Login Portal
</a>

</div>

</div>

<section class="hero">

<div class="hero-content">

<h1>
Modern Learning Management System
</h1>

<p>
Manage students, courses, deferrals,
audit trails and academic workflows
using Mentari LMS.
</p>

<a class="hero-btn"
href="/Mentari/app/auth/login">

Get Started

</a>

</div>

</section>

<section class="features">

<div class="feature-card">

<i class="fas fa-user-graduate"></i>

<h3>Student Management</h3>

<p>
Register and manage students easily.
</p>

</div>

<div class="feature-card">

<i class="fas fa-book"></i>

<h3>Course Management</h3>

<p>
Create and organize academic courses.
</p>

</div>

<div class="feature-card">

<i class="fas fa-clock"></i>

<h3>Deferral System</h3>

<p>
Students can request deferrals and
admins can approve or reject.
</p>

</div>

</section>

</body>
</html>
""");
    }
}