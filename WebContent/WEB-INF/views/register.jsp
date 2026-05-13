<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("pageTitle", "Register - Portfolio Management System"); %>
<jsp:include page="header.jsp" />

<div class="auth-wrap">
    <div class="auth-card wide">
        <h2>Create Account</h2>
        <p class="muted">Get &#8377;1,00,000 of virtual cash to start trading</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/register" method="post" id="registerForm" onsubmit="return validateRegister()">
            <div class="grid-2">
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" required>
                    <span class="error-msg" id="fullNameError"></span>
                </div>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required onblur="checkUsername(this.value)">
                    <span class="error-msg" id="usernameError"></span>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                    <span class="error-msg" id="emailError"></span>
                </div>
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" id="phone" name="phone" maxlength="10">
                    <span class="error-msg" id="phoneError"></span>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required onkeyup="checkPasswordStrength(this.value)">
                    <div class="password-strength" id="passwordStrength"></div>
                    <span class="error-msg" id="passwordError"></span>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                    <span class="error-msg" id="confirmError"></span>
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>

        <p class="auth-link">Already have an account? <a href="<%= request.getContextPath() %>/login">Sign in</a></p>
    </div>
</div>

<jsp:include page="footer.jsp" />
