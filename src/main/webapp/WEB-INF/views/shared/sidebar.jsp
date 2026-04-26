<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    :root {
        --sidebar-bg: #2c3e50;
        --sidebar-hover: #34495e;
        --accent-blue: #3498db;
        --text-dim: #bdc3c7;
        --danger-red: #e74c3c;
    }

    .sidebar {
        width: 260px;
        background-color: var(--sidebar-bg);
        color: white;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        box-shadow: 2px 0 10px rgba(0,0,0,0.2);
        position: fixed;
        left: 0;
        top: 0;
        z-index: 1000;
    }

    .sidebar h3 {
        padding: 30px 25px;
        margin: 0;
        font-size: 1.3rem;
        font-weight: 700;
        letter-spacing: 1px;
        color: var(--accent-blue);
        display: flex;
        align-items: center;
        gap: 12px;
        border-bottom: 1px solid rgba(255,255,255,0.05);
    }

    .sidebar ul {
        list-style: none;
        padding: 15px 0;
        margin: 0;
        flex-grow: 1;
    }

    .sidebar ul li a {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 14px 25px;
        color: var(--text-dim);
        text-decoration: none;
        transition: all 0.25s ease-in-out;
        font-size: 0.95rem;
        border-left: 4px solid transparent;
    }

    .sidebar ul li a:hover, .nav-link.active {
        background-color: var(--sidebar-hover);
        color: white;
        border-left: 4px solid var(--accent-blue);
    }

    .nav-link.active {
        background-color: rgba(52, 152, 219, 0.2);
        color: #3498db !important;
    }

    .logout-item {
        margin-top: auto !important;
        border-top: 1px solid rgba(255,255,255,0.05);
        padding-bottom: 20px;
    }

    .logout-item a {
        color: var(--danger-red) !important;
    }

    body {
        margin-left: 260px;
    }
</style>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll(".nav-link");

        navLinks.forEach(link => {
            // Check if the current URL ends with the link's href
            if (currentPath.includes(link.getAttribute("href"))) {
                link.classList.add("active");
            }
        });
    });
</script>

<div class="sidebar">
    <h3><i class="fas fa-graduation-cap"></i> Mentari LMS</h3>
    <ul id="nav-links">

        <c:choose>
            <%-- ADMIN LINKS --%>
            <c:when test="${sessionScope.loggedInUser.role == 'ADMIN'}">
                <li>
                    <a href="${pageContext.request.contextPath}/admin-dashboard" class="nav-link">
                        <i class="fas fa-chart-line"></i> Dashboard
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/students" class="nav-link">
                        <i class="fas fa-user-graduate"></i> Manage Students
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/courses" class="nav-link">
                        <i class="fas fa-book"></i> Manage Courses
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/deferrals" class="nav-link">
                        <i class="fas fa-clipboard-list"></i> Review Requests
                    </a>
                </li>
            </c:when>

            <%-- STUDENT LINKS --%>
            <c:otherwise>
                <li>
                    <a href="${pageContext.request.contextPath}/student/portal" class="nav-link">
                        <i class="fas fa-chart-line"></i> My Dashboard
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/student/my-courses" class="nav-link">
                        <i class="fas fa-book-reader"></i> My Courses
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/student/defer" class="nav-link">
                        <i class="fas fa-calendar-alt"></i> Request Deferral
                    </a>
                </li>
                <li>
                        <a href="${pageContext.request.contextPath}/student/account" class="nav-link">
                            <i class="fas fa-user-cog"></i> Manage Password
                        </a>
                    </li>
            </c:otherwise>
        </c:choose>

        <li class="logout-item">
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </li>
    </ul>
</div>