package app.framework.rendering;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthPageRenderer {

    public String loginPage(String errorMessage) {

        String errorHtml = "";

        if (errorMessage != null) {

            errorHtml = """
<div class='error-box'>
%s
</div>
""".formatted(errorMessage);
        }

        return """
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Mentari LMS Login</title>

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
height:100vh;
display:flex;
justify-content:center;
align-items:center;
background:
linear-gradient(
rgba(15,23,42,0.75),
rgba(15,23,42,0.75)
),
url('https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=2070');

background-size:cover;
background-position:center;
}

.login-container{
width:100%%;
max-width:430px;
background:white;
padding:40px;
border-radius:20px;
box-shadow:0 10px 40px rgba(0,0,0,0.2);
}

.logo{
text-align:center;
font-size:32px;
font-weight:bold;
color:#2563eb;
margin-bottom:10px;
}

.subtitle{
text-align:center;
color:#6b7280;
margin-bottom:30px;
font-size:15px;
}

.form-group{
margin-bottom:20px;
}

label{
display:block;
margin-bottom:8px;
font-weight:600;
color:#111827;
}

.input-box{
position:relative;
}

.input-box i{
position:absolute;
left:15px;
top:50%%;
transform:translateY(-50%%);
color:#6b7280;
}

input{
width:100%%;
padding:14px 14px 14px 45px;
border:1px solid #d1d5db;
border-radius:12px;
font-size:15px;
transition:0.2s;
}

input:focus{
outline:none;
border-color:#2563eb;
box-shadow:0 0 0 4px rgba(37,99,235,0.1);
}

.login-btn{
width:100%%;
padding:14px;
border:none;
border-radius:12px;
background:#2563eb;
color:white;
font-size:16px;
font-weight:bold;
cursor:pointer;
transition:0.2s;
margin-top:10px;
}

.login-btn:hover{
background:#1d4ed8;
}

.back-home{
display:block;
text-align:center;
margin-top:20px;
text-decoration:none;
color:#2563eb;
font-size:14px;
font-weight:600;
}

.error-box{
background:#fee2e2;
color:#dc2626;
padding:14px;
border-radius:12px;
margin-bottom:20px;
font-size:14px;
text-align:center;
}

</style>

</head>

<body>

<div class="login-container">

<div class="logo">
<i class="fas fa-graduation-cap"></i>
Mentari LMS
</div>

<div class="subtitle">
Login to continue
</div>

%s

<form method="post"
      action="/Mentari/app/auth/login">

<div class="form-group">

<label>
Username
</label>

<div class="input-box">

<i class="fas fa-user"></i>

<input type="text"
       name="username"
       placeholder="Enter username"
       required>

</div>

</div>

<div class="form-group">

<label>
Password
</label>

<div class="input-box">

<i class="fas fa-lock"></i>

<input type="password"
       name="password"
       placeholder="Enter password"
       required>

</div>

</div>

<button type="submit"
        class="login-btn">

Login

</button>

</form>

<a class="back-home"
   href="/Mentari/">

← Back to Homepage

</a>

</div>

</body>
</html>
""".formatted(errorHtml);
    }
}