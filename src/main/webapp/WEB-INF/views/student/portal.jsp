<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Portal | Mentari LMS</title>
</head>
<body style="display: flex;">
    <jsp:include page="../shared/sidebar.jsp" />
    <main style="flex-grow: 1; padding: 40px;">
        <h1>Student Portal</h1>
        <div class="card">
            <h3>Welcome back, ${loggedInUser.username}!</h3>
        </div>
    </main>
</body>
</html>