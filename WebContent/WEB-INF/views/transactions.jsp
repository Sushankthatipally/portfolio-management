<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.pms.model.Transaction, java.text.SimpleDateFormat"%>
<% request.setAttribute("pageTitle", "Transactions - Portfolio Management System"); %>
<jsp:include page="header.jsp" />

<%
    List<Transaction> txs = (List<Transaction>) request.getAttribute("transactions");
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
%>

<section class="page-head">
    <h1>Transaction History</h1>
    <p class="muted">All your buy &amp; sell orders</p>
</section>

<section class="panel">
    <% if (txs == null || txs.isEmpty()) { %>
        <div class="empty"><p>No transactions yet.</p></div>
    <% } else { %>
    <table class="table">
        <thead>
            <tr>
                <th>Date</th>
                <th>Symbol</th>
                <th>Company</th>
                <th>Type</th>
                <th class="num">Qty</th>
                <th class="num">Price</th>
                <th class="num">Total</th>
            </tr>
        </thead>
        <tbody>
            <% for (Transaction t : txs) { %>
            <tr>
                <td><%= sdf.format(t.getTransactionDate()) %></td>
                <td><strong><%= t.getSymbol() %></strong></td>
                <td><%= t.getCompanyName() %></td>
                <td>
                    <span class="badge <%= "BUY".equals(t.getTransactionType()) ? "badge-success" : "badge-danger" %>">
                        <%= t.getTransactionType() %>
                    </span>
                </td>
                <td class="num"><%= t.getQuantity() %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", t.getPrice()) %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", t.getTotalAmount()) %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } %>
</section>

<jsp:include page="footer.jsp" />
