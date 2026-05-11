<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Request Deferral | Mentari LMS</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-blue: #007bff; --light-bg: #f8f9fa; }
        body { font-family: 'Segoe UI', sans-serif; background: var(--light-bg); margin: 0; display: flex; }
        .main-content { flex-grow: 1; padding: 40px; min-height: 100vh; box-sizing: border-box; }
        .content-card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); margin-bottom: 30px; }
        .header-flex { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; border-bottom: 2px solid #ddd; padding-bottom: 15px; }
        h2 { margin: 0; color: #2c3e50; }
        .form-group { margin-bottom: 18px; }
        .form-label { display: block; margin-bottom: 6px; font-weight: 600; color: #4a5568; font-size: 0.9rem; }
        .form-control { width: 100%; padding: 11px 14px; border: 1.5px solid #e2e8f0; border-radius: 8px; font-size: 0.95rem; box-sizing: border-box; }
        .form-control:focus { outline: none; border-color: #007bff; box-shadow: 0 0 0 3px rgba(0,123,255,0.15); }
        .btn-submit { background: #007bff; color: white; padding: 12px 28px; border: none; border-radius: 8px; font-weight: 700; cursor: pointer; font-size: 1rem; }
        .btn-submit:hover { background: #0069d9; }
        table { width: 100%; border-collapse: collapse; }
        table th { text-align: left; padding: 12px 15px; background: #f1f3f5; color: #495057; font-weight: 600; }
        table td { padding: 12px 15px; border-bottom: 1px solid #dee2e6; }
        .badge-pending  { background: #fff3cd; color: #856404; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .badge-approved { background: #d1e7dd; color: #0a3622; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .badge-rejected { background: #f8d7da; color: #58151c; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
        .alert-success { background: #d1e7dd; color: #0a3622; padding: 12px; border-radius: 8px; margin-bottom: 20px; border-left: 5px solid #198754; }
    </style>
</head>
<body>
    <jsp:include page="../shared/sidebar.jsp" />
    <main class="main-content">

        <%-- Request Form --%>
        <div class="content-card">
            <div class="header-flex">
                <h2><i class="fas fa-calendar-alt"></i> Request a Deferral</h2>
            </div>

            <c:if test="${param.success eq 'true'}">
                <div class="alert-success">
                    <i class="fas fa-check-circle"></i> Your deferral request has been submitted. The admin will review it shortly.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/student/defer" method="POST">
                <div class="form-group">
                    <label class="form-label"><i class="fas fa-calendar-day"></i> Start Date</label>
                    <input type="date" name="startDate" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label"><i class="fas fa-calendar-check"></i> End Date</label>
                    <input type="date" name="endDate" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label"><i class="fas fa-comment-alt"></i> Reason for Deferral</label>
                    <textarea name="reason" class="form-control" rows="4"
                              placeholder="Please explain why you are requesting a deferral..."
                              required></textarea>
                </div>
                <button type="submit" class="btn-submit">
                    <i class="fas fa-paper-plane"></i> Submit Request
                </button>
            </form>
        </div>

        <%-- Student's own requests --%>
        <div class="content-card">
            <div class="header-flex">
                <h2><i class="fas fa-history"></i> My Requests</h2>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Reason</th>
                        <th>Submitted</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${myDeferrals}">
                        <tr>
                            <td>${d.startDate}</td>
                            <td>${d.endDate}</td>
                            <td style="color:#555;">${d.reason}</td>
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
                        </tr>
                    </c:forEach>
                    <c:if test="${empty myDeferrals}">
                        <tr>
                            <td colspan="5" style="text-align:center; padding:20px; color:#999;">
                                You have not submitted any deferral requests yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </main>
</body>
</html>