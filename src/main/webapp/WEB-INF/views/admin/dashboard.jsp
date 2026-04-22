<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Mentari LMS</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-blue: #007bff;
            --dark-sidebar: #2c3e50;
            --light-bg: #f8f9fa;
            --text-main: #333;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            background-color: var(--light-bg);
            color: var(--text-main);
        }

        /* Content Area */
        .main-content {
            flex-grow: 1;
            padding: 40px;
            min-height: 100vh;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 2px solid #ddd;
            padding-bottom: 10px;
        }

        /* Stats Grid */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            transition: transform 0.2s;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .card h3 {
            margin: 0;
            color: #7f8c8d;
            font-size: 0.9rem;
            text-transform: uppercase;
        }

        .card .value {
            font-size: 2rem;
            font-weight: bold;
            color: var(--dark-sidebar);
            margin: 10px 0;
        }

        .status-badge {
            background: #e1f5fe;
            color: #01579b;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }

        .icon-box {
            float: right;
            font-size: 2.5rem;
            color: var(--primary-blue);
            opacity: 0.2;
        }
    </style>
</head>
<body>

    <jsp:include page="../shared/sidebar.jsp" />

    <main class="main-content">
        <div class="header">
            <div>
                <h1>Mentari Admin Dashboard</h1>
                <span class="status-badge">
                    <i class="fas fa-circle" style="font-size: 8px; margin-right: 5px;"></i>
                    System Status: Active
                </span>
            </div>
            <div class="user-info">
                <strong>Welcome, ${loggedInUser.username}</strong>
            </div>
        </div>

        <div class="stats-grid">
            <%-- DYNAMIC TOTAL STUDENTS CARD --%>
            <div class="card">
                <i class="fas fa-users icon-box"></i>
                <h3>Total Students</h3>
                <p class="value">
                    <c:out value="${totalStudents}" default="0" />
                </p>
                <small style="color: green;">Live Registry Count</small>
            </div>

            <div class="card">
                <i class="fas fa-book-open icon-box"></i>
                <h3>Active Courses</h3>
                <p class="value">18</p>
                <small>4 Pending updates</small>
            </div>

            <div class="card">
                <i class="fas fa-tasks icon-box"></i>
                <h3>Pending Requests</h3>
                <p class="value">7</p>
                <small style="color: #e67e22;">Requires attention</small>
            </div>
        </div>

        <section style="margin-top: 40px;">
            <h2>Recent Activity</h2>
            <div class="card" style="padding: 15px;">
                <p>User <strong>admin</strong> updated database schema successfully.</p>
                <p>Default admin verified via <strong>@h.k_rajah8</strong> credentials.</p>
            </div>
        </section>
    </main>

</body>
</html>