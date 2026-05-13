<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("pageTitle", "Login - Portfolio Management System"); %>
<jsp:include page="header.jsp" />

<div class="auth-wrap">
    <div class="auth-card">
        <h2>Welcome Back</h2>
        <p class="muted">Sign in to manage your portfolio</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="post" id="loginForm" onsubmit="return validateLogin()">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required
                       value="<%= request.getAttribute("rememberedUser") != null ? request.getAttribute("rememberedUser") : "" %>">
                <span class="error-msg" id="usernameError"></span>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                <span class="error-msg" id="passwordError"></span>
            </div>
            <div class="form-row">
                <label class="checkbox-label">
                    <input type="checkbox" name="remember"> Remember me
                </label>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>

        <p class="auth-link">New here? <a href="<%= request.getContextPath() %>/register">Create an account</a></p>
        <p class="muted small">Demo: admin / admin123 &middot; john / john123</p>
    </div>
</div>

<jsp:include page="footer.jsp" />
