<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<% request.setAttribute("pageTitle", "Error - Portfolio Management System"); %>
<jsp:include page="header.jsp" />
<div class="auth-wrap">
    <div class="auth-card">
        <h2>Something went wrong</h2>
        <p class="muted">An unexpected error occurred.</p>
        <% if (exception != null) { %>
            <pre><%= exception.getMessage() %></pre>
        <% } %>
        <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-primary">Back to dashboard</a>
    </div>
</div>
<jsp:include page="footer.jsp" />
