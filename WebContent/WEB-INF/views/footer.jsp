<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</main>
<footer class="site-footer">
    <div class="container">
        <p>&copy; <%= java.time.Year.now() %> Portfolio Management System. Built with Servlets &amp; JSP using MVC.</p>
        <p class="small">Session ID: <%= session.getId().substring(0, 8) %>... | App: <%= application.getAttribute("appName") != null ? application.getAttribute("appName") : "Portfolio Management System" %></p>
    </div>
</footer>
</body>
</html>
