<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Students | Mentari LMS</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-blue: #007bff;
            --light-bg: #f8f9fa;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-bg);
            margin: 0;
            display: flex; /* Matches your dashboard */
        }

        /* Fixed Logic: Matches Dashboard's flex-grow */
        .main-content {
            flex-grow: 1;
            padding: 40px;
            min-height: 100vh;
            background-color: var(--light-bg);
            box-sizing: border-box;
        }

        /* Added a wrapper for the table like your dashboard cards */
        .content-card {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }

        .header-flex {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            border-bottom: 2px solid #ddd;
            padding-bottom: 15px;
        }

        h2 {
            margin: 0;
            color: #2c3e50;
        }

        /* Table Styling */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        table th {
            text-align: left;
            padding: 15px;
            background-color: #f1f3f5;
            color: #495057;
            font-weight: 600;
        }

        table td {
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
        }

        .btn-add {
            background-color: var(--primary-blue);
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            transition: opacity 0.2s;
        }

        .btn-add:hover {
            opacity: 0.9;
        }

        .success-msg {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 5px solid #28a745;
        }
    </style>
</head>
<body>

    <jsp:include page="../shared/sidebar.jsp" />

    <main class="main-content">
        <div class="content-card">
            <c:choose>
                <c:when test="${param.action == 'new'}">
                    <div class="header-flex">
                        <h2><i class="fas fa-user-plus"></i> Register Student</h2>
                    </div>

                    <form action="${pageContext.request.contextPath}/admin/students" method="POST">
                        <div style="margin-bottom: 15px;">
                            <label style="display:block; margin-bottom:5px; font-weight:600;">Full Name</label>
                            <input type="text" name="fullName" style="width:100%; padding:12px; border:1px solid #ddd; border-radius:6px;" required>
                        </div>
                        <div style="margin-bottom: 15px;">
                            <label style="display:block; margin-bottom:5px; font-weight:600;">Email</label>
                            <input type="email" name="email" style="width:100%; padding:12px; border:1px solid #ddd; border-radius:6px;" required>
                        </div>
                        <div style="margin-bottom: 20px;">
                            <label style="display:block; margin-bottom:5px; font-weight:600;">Registration Number</label>
                            <input type="text" name="regNumber" style="width:100%; padding:12px; border:1px solid #ddd; border-radius:6px;" required>
                        </div>
                        <button type="submit" class="btn-add" style="border:none; cursor:pointer;">Save Student</button>
                        <a href="${pageContext.request.contextPath}/admin/students" style="margin-left:15px; color:#6c757d; text-decoration:none;">Cancel</a>
                    </form>
                </c:when>

                <c:otherwise>
                    <div class="header-flex">
                        <h2><i class="fas fa-users"></i> Registered Students</h2>
                        <a href="${pageContext.request.contextPath}/admin/students?action=new" class="btn-add">
                            <i class="fas fa-plus"></i> New Student
                        </a>
                    </div>

                    <c:if test="${param.success == 'true'}">
                        <div class="success-msg">
                            <i class="fas fa-check-circle"></i> Student successfully added to the system.
                        </div>
                    </c:if>

                    <table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Reg Number</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <td><strong>${s.fullName}</strong></td>
                                    <td>${s.email}</td>
                                    <td><code>${s.regNumber}</code></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty students}">
                                <tr><td colspan="3" style="text-align:center; padding: 30px; color: #999;">No student records found.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

</body>
</html>