<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .account-container {
        max-width: 500px;
        margin: 50px auto;
        background: #ffffff;
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.05);
        border: 1px solid #e1e4e8;
    }

    .account-header {
        text-align: center;
        margin-bottom: 25px;
    }

    .account-header h2 {
        color: #1a202c;
        font-size: 1.5rem;
        font-weight: 700;
        margin-bottom: 8px;
    }

    .account-header p {
        color: #718096;
        font-size: 0.9rem;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: #4a5568;
        font-size: 0.85rem;
    }

    .form-control {
        width: 100%;
        padding: 12px 16px;
        border: 1.5px solid #e2e8f0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.2s ease;
        box-sizing: border-box;
    }

    .form-control:focus {
        outline: none;
        border-color: #4299e1;
        box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.15);
    }

    .btn-update {
        width: 100%;
        background-color: #3182ce;
        color: white;
        padding: 12px;
        border: none;
        border-radius: 8px;
        font-weight: 700;
        font-size: 1rem;
        cursor: pointer;
        transition: background 0.2s ease;
        margin-top: 10px;
    }

    .btn-update:hover {
        background-color: #2b6cb0;
    }

    /* Message Styling */
    .alert {
        padding: 12px;
        border-radius: 8px;
        margin-bottom: 20px;
        font-size: 0.85rem;
        text-align: center;
    }
    .alert-error {
        background-color: #fff5f5;
        color: #c53030;
        border: 1px solid #feb2b2;
    }
</style>

<div class="account-container">
    <div class="account-header">
        <h2>Account Settings</h2>
        <p>Ensure your account remains secure by updating your password.</p>
    </div>

    <%-- Error Handling UI --%>
    <c:if test="${not empty param.error}">
        <div class="alert alert-error">
            <i class="fas fa-exclamation-circle"></i>
            Incorrect current password. Please try again.
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/student/account" method="POST">
        <div class="form-group">
            <label for="currentPassword">Current Password</label>
            <input type="password" id="currentPassword" name="currentPassword"
                   placeholder="Enter default: @Mentari2026" required class="form-control">
        </div>

        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword"
                   placeholder="Create a strong password" required class="form-control">
        </div>

        <button type="submit" class="btn-update">
            <i class="fas fa-key"></i> Update Password
        </button>
    </form>
</div>