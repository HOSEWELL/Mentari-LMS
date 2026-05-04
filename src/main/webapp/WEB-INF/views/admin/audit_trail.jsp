<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Audit Trail | Mentari LMS</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-blue: #007bff; --light-bg: #f8f9fa; }
        body { font-family: 'Segoe UI', sans-serif; background: var(--light-bg); margin: 0; display: flex; }
        .main-content { flex-grow: 1; padding: 40px; min-height: 100vh; box-sizing: border-box; }
        .content-card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
        .header-flex { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; border-bottom: 2px solid #ddd; padding-bottom: 15px; }
        h2 { margin: 0; color: #2c3e50; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table th { text-align: left; padding: 15px; background: #f1f3f5; color: #495057; font-weight: 600; }
        table td { padding: 15px; border-bottom: 1px solid #dee2e6; }
        .badge { background: #e3f2fd; color: #1565c0; padding: 4px 10px; border-radius: 20px; font-size: 0.8rem; }
    </style>
</head>
<body>
    <jsp:include page="../shared/sidebar.jsp" />
    <main class="main-content">
        <div class="content-card">
            <div class="header-flex">
                <h2><i class="fas fa-clipboard-list"></i> Audit Trail</h2>
                <span class="badge">${logs.size()} entries</span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Audit No</th>
                        <th>Activity</th>
                        <th>Timestamp</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${logs}">
                        <tr>
                            <td><code>${log.id}</code></td>
                            <td>${log.activity}</td>
                            <td><small style="color:#666;">${log.createdAt}</small></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty logs}">
                        <tr>
                            <td colspan="3" style="text-align:center; padding:30px; color:#999;">
                                No activity recorded yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>