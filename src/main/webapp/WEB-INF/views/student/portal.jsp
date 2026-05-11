<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Portal | Mentari LMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            background: #f4f7f6;
        }

        .main-content {
            padding: 20px;
            width: 100%;
            box-sizing: border-box;
        }

        .welcome-card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        @media (max-width: 768px) {
            .sidebar {
                display: none;
            }

            .main-content {
                padding: 15px;
            }
        }
    </style>
</head>

<body>
<div style="display: flex;">
    <jsp:include page="../shared/sidebar.jsp" />

    <main class="main-content">
        <header>
            <h1>Student Portal</h1>
            <p>Manage your learning journey</p>
        </header>

        <div class="welcome-card">
            <h3><i class="fas fa-graduation-cap"></i> Welcome back, ${loggedInUser.username}!</h3>
            <hr>

            <div class="stats-grid" style="display: flex; gap: 20px; flex-wrap: wrap;">

                <!-- My Courses -->
                <div style="flex: 1; min-width: 200px; background: #e3f2fd; padding: 15px; border-radius: 8px;">
                    <h4>My Courses</h4>
                    <p>You are currently enrolled in <strong>1</strong> course.</p>
                    <a href="${pageContext.request.contextPath}/student/my-courses" class="btn">
                        View Courses
                    </a>
                </div>

                <!-- Deferrals -->
                <div style="flex: 1; min-width: 200px; background: #fff3e0; padding: 15px; border-radius: 8px;">
                    <h4>Deferrals</h4>
                    <p>Need a deferral? Submit a request here.</p>
                    <a href="${pageContext.request.contextPath}/student/defer" class="btn">
                        Request Now
                    </a>
                </div>

            </div>
        </div>
    </main>
</div>
</body>
</html>