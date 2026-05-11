<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Deferral Requests | Mentari LMS</title>
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
        table td { padding: 15px; border-bottom: 1px solid #dee2e6; vertical-align: middle; }
        .badge-pending  { background: #fff3cd; color: #856404; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .badge-approved { background: #d1e7dd; color: #0a3622; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .badge-rejected { background: #f8d7da; color: #58151c; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .btn-approve { background: #198754; color: white; border: none; padding: 7px 14px; border-radius: 6px; cursor: pointer; font-size: 0.85rem; }
        .btn-reject  { background: #dc3545; color: white; border: none; padding: 7px 14px; border-radius: 6px; cursor: pointer; font-size: 0.85rem; margin-left: 6px; }
        .btn-approve:hover { background: #157347; }
        .btn-reject:hover  { background: #b02a37; }
        .alert-success { background: #d1e7dd; color: #0a3622; padding: 12px; border-radius: 8px; margin-bottom: 20px; border-left: 5px solid #198754; }
    </style>
</head>
<body>
    <jsp:include page="../shared/sidebar.jsp" />
    <main class="main-content">
        <div class="content-card">
            <div class="header-flex">
                <h2><i class="fas fa-clipboard-list"></i> Deferral Requests</h2>
                <span style="color:#6c757d; font-size:0.9rem;">${deferrals.size()} total requests</span>
            </div>

            <c:if test="${param.success eq 'true'}">
                <div class="alert-success">
                    <i class="fas fa-check-circle"></i> Request status updated successfully.
                </div>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th>Student</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Reason</th>
                        <th>Submitted</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${deferrals}">
                        <tr>
                            <td><strong>${d.studentName}</strong></td>
                            <td>${d.startDate}</td>
                            <td>${d.endDate}</td>
                            <td style="max-width:200px; color:#555;">${d.reason}</td>
                            <td><small style="color:#888;">${d.submittedAt}</small></td>
                            <td>
                                <c:choose>
                                    <c:when test="${d.status eq 'PENDING'}">
                                        <span class="badge-pending"><i class="fas fa-clock"></i> Pending</span>
                                    </c:when>
                                    <c:when test="${d.status eq 'APPROVED'}">
                                        <span class="badge-approved"><i class="fas fa-check"></i> Approved</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-rejected"><i class="fas fa-times"></i> Rejected</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${d.status eq 'PENDING'}">
                                    <form action="${pageContext.request.contextPath}/admin/deferrals"
                                          method="POST" style="display:inline;">
                                        <input type="hidden" name="id" value="${d.id}"/>
                                        <input type="hidden" name="studentName" value="${d.studentName}"/>
                                        <input type="hidden" name="action" value="approve"/>
                                        <button type="submit" class="btn-approve">
                                            <i class="fas fa-check"></i> Approve
                                        </button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/deferrals"
                                          method="POST" style="display:inline;">
                                        <input type="hidden" name="id" value="${d.id}"/>
                                        <input type="hidden" name="studentName" value="${d.studentName}"/>
                                        <input type="hidden" name="action" value="reject"/>
                                        <button type="submit" class="btn-reject">
                                            <i class="fas fa-times"></i> Reject
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${d.status ne 'PENDING'}">
                                    <span style="color:#aaa; font-size:0.85rem;">No action needed</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty deferrals}">
                        <tr>
                            <td colspan="7" style="text-align:center; padding:30px; color:#999;">
                                No deferral requests submitted yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>