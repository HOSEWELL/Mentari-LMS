<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Courses | Mentari LMS</title>
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
            display: flex;
        }

        .main-content {
            flex-grow: 1;
            padding: 40px;
            min-height: 100vh;
            background-color: var(--light-bg);
            box-sizing: border-box;
        }

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

        h2 { margin: 0; color: #2c3e50; }

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

        .btn-add:hover { opacity: 0.9; }

        .success-msg {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 5px solid #28a745;
        }

        .form-group { margin-bottom: 15px; }
        .form-label { display:block; margin-bottom:5px; font-weight:600; }
        .form-control { width:100%; padding:12px; border:1px solid #ddd; border-radius:6px; box-sizing: border-box; }
    </style>
</head>
<body>

    <jsp:include page="../shared/sidebar.jsp" />

    <main class="main-content">
        <div class="content-card">
            <c:choose>
                <%-- Registration Form View --%>
                <c:when test="${param.action == 'new'}">
                    <div class="header-flex">
                        <h2><i class="fas fa-book-medical"></i> Register New Course</h2>
                    </div>

                    <form action="${pageContext.request.contextPath}/admin/courses" method="POST">
                        <div class="form-group">
                            <label class="form-label">Course Name</label>
                            <input type="text" name="name" class="form-control" placeholder="e.g. Java Enterprise Edition" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Course Code</label>
                            <input type="text" name="code" class="form-control" placeholder="e.g. JEE-101" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Description</label>
                            <textarea name="description" class="form-control" rows="4" placeholder="Briefly describe the course objectives..."></textarea>
                        </div>
                        <button type="submit" class="btn-add" style="border:none; cursor:pointer;">Save Course</button>
                        <a href="${pageContext.request.contextPath}/admin/courses" style="margin-left:15px; color:#6c757d; text-decoration:none;">Cancel</a>
                    </form>
                </c:when>

                <%-- Course Table List View --%>
                <c:otherwise>
                    <div class="header-flex">
                        <h2><i class="fas fa-book"></i> Course Catalog</h2>
                        <a href="${pageContext.request.contextPath}/admin/courses?action=new" class="btn-add">
                            <i class="fas fa-plus"></i> New Course
                        </a>
                    </div>

                    <%-- Success or Error message --%>
                    <c:if test="${param.success eq 'true'}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle"></i> Course has been successfully added to the catalog.
                        </div>
                    </c:if>

                    <c:if test="${param.error eq 'invalid_name'}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-triangle"></i> <strong>Error:</strong> Course name must be at least 5 characters long.
                        </div>
                    </c:if>

                    <table>
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Course Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${courses}">
                                <tr>
                                    <td><code>${c.code}</code></td>
                                    <td><strong>${c.name}</strong></td>
                                    <td>${c.description}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty courses}">
                                <tr><td colspan="3" style="text-align:center; padding: 30px; color: #999;">No courses available in the system.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

</body>
</html>