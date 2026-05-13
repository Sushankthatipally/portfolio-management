<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pms.model.User"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "Portfolio Management System" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/script.js" defer></script>
    <script src="<%= request.getContextPath() %>/js/validation.js" defer></script>
</head>
<body>
<header class="topbar">
    <div class="container nav">
        <a class="brand" href="<%= request.getContextPath() %>/dashboard">Portfolio<span>Management</span></a>
        <% if (loggedInUser != null) { %>
        <nav class="navlinks">
            <ul>
                <li><a href="<%= request.getContextPath() %>/dashboard">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/market">Market</a></li>
                <li><a href="<%= request.getContextPath() %>/transactions">Transactions</a></li>
                <li class="dropdown">
                    <a href="#" class="dropbtn"><%= loggedInUser.getFullName() %> &#9662;</a>
                    <div class="dropdown-content">
                        <span class="dd-info">Balance: &#8377; <%= String.format("%,.2f", loggedInUser.getBalance()) %></span>
                        <a href="<%= request.getContextPath() %>/logout">Logout</a>
                    </div>
                </li>
            </ul>
        </nav>
        <% } %>
    </div>
</header>
<main class="container">
