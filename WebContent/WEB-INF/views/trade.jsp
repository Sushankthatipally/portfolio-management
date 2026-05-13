<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pms.model.Stock, com.pms.model.Holding, com.pms.model.User"%>
<% request.setAttribute("pageTitle", "Trade - Portfolio Management System"); %>
<jsp:include page="header.jsp" />

<%
    Stock stock = (Stock) request.getAttribute("stock");
    String action = (String) request.getAttribute("action");
    Holding holding = (Holding) request.getAttribute("holding");
    User u = (User) session.getAttribute("user");
    boolean isBuy = "BUY".equalsIgnoreCase(action);
    int maxQty = isBuy ? (int) (u.getBalance() / stock.getCurrentPrice()) : (holding != null ? holding.getQuantity() : 0);
%>

<section class="page-head">
    <h1><%= action %> <%= stock.getSymbol() %></h1>
    <p class="muted"><%= stock.getCompanyName() %> &middot; <%= stock.getSector() %></p>
</section>

<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
<% } %>

<div class="trade-grid">
    <div class="panel">
        <h3>Order Details</h3>

        <form method="post" action="<%= request.getContextPath() %>/trade" id="tradeForm" onsubmit="return validateTrade(<%= maxQty %>)">
            <input type="hidden" name="stockId" value="<%= stock.getStockId() %>">
            <input type="hidden" name="action" value="<%= action %>">

            <div class="form-group">
                <label>Current Price</label>
                <div class="readonly-field">&#8377; <%= String.format("%,.2f", stock.getCurrentPrice()) %></div>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity (max <%= maxQty %>)</label>
                <input type="number" id="quantity" name="quantity" min="1" max="<%= maxQty %>" value="1"
                       oninput="updateTotal(<%= stock.getCurrentPrice() %>)">
                <span class="error-msg" id="quantityError"></span>
            </div>

            <div class="form-group">
                <label>Estimated Total</label>
                <div class="readonly-field" id="estTotal">&#8377; <%= String.format("%,.2f", stock.getCurrentPrice()) %></div>
            </div>

            <% if (isBuy) { %>
                <button type="submit" class="btn btn-success btn-block">Confirm Buy</button>
            <% } else { %>
                <button type="submit" class="btn btn-danger btn-block">Confirm Sell</button>
            <% } %>
            <a href="<%= request.getContextPath() %>/market" class="btn btn-outline btn-block">Cancel</a>
        </form>
    </div>

    <div class="panel">
        <h3>Stock Details</h3>
        <ul class="info-list">
            <li><span>Symbol</span><strong><%= stock.getSymbol() %></strong></li>
            <li><span>Company</span><strong><%= stock.getCompanyName() %></strong></li>
            <li><span>Sector</span><strong><%= stock.getSector() %></strong></li>
            <li><span>Current Price</span><strong>&#8377; <%= String.format("%,.2f", stock.getCurrentPrice()) %></strong></li>
            <li><span>Previous Close</span><strong>&#8377; <%= String.format("%,.2f", stock.getPreviousClose()) %></strong></li>
            <li><span>Day Change</span>
                <strong class="<%= stock.getChange() >= 0 ? "positive" : "negative" %>">
                    <%= String.format("%+.2f", stock.getChange()) %> (<%= String.format("%+.2f", stock.getChangePercent()) %>%)
                </strong>
            </li>
            <li><span>Market Cap</span><strong><%= stock.getMarketCap() %></strong></li>
        </ul>

        <% if (holding != null) { %>
        <h4 class="mt">Your Position</h4>
        <ul class="info-list">
            <li><span>Quantity Owned</span><strong><%= holding.getQuantity() %></strong></li>
            <li><span>Average Cost</span><strong>&#8377; <%= String.format("%,.2f", holding.getAvgBuyPrice()) %></strong></li>
        </ul>
        <% } %>

        <h4 class="mt">Account</h4>
        <ul class="info-list">
            <li><span>Available Cash</span><strong>&#8377; <%= String.format("%,.2f", u.getBalance()) %></strong></li>
        </ul>
    </div>
</div>

<jsp:include page="footer.jsp" />
