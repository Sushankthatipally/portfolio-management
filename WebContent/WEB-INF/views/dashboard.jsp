<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.pms.model.*"%>
<% request.setAttribute("pageTitle", "Dashboard - Portfolio Management System"); %>
<jsp:include page="header.jsp" />

<jsp:useBean id="loggedUser" class="com.pms.model.User" scope="session" />

<%
    List<Holding> holdings = (List<Holding>) request.getAttribute("holdings");
    List<Stock> topStocks = (List<Stock>) request.getAttribute("topStocks");
    double invested = (Double) request.getAttribute("investedValue");
    double current = (Double) request.getAttribute("currentValue");
    double pl = (Double) request.getAttribute("totalPL");
    double plPercent = invested > 0 ? (pl / invested) * 100 : 0;
    String flash = (String) session.getAttribute("flashMessage");
    if (flash != null) session.removeAttribute("flashMessage");
%>

<% if (flash != null) { %>
    <div class="alert alert-success"><%= flash %></div>
<% } %>

<section class="page-head">
    <h1>Welcome, <jsp:getProperty name="loggedUser" property="fullName" />!</h1>
    <p class="muted">Here's the snapshot of your portfolio</p>
</section>

<section class="cards">
    <div class="card stat">
        <span class="card-label">Available Cash</span>
        <span class="card-value">&#8377; <%= String.format("%,.2f", loggedUser.getBalance()) %></span>
    </div>
    <div class="card stat">
        <span class="card-label">Invested Value</span>
        <span class="card-value">&#8377; <%= String.format("%,.2f", invested) %></span>
    </div>
    <div class="card stat">
        <span class="card-label">Current Value</span>
        <span class="card-value">&#8377; <%= String.format("%,.2f", current) %></span>
    </div>
    <div class="card stat <%= pl >= 0 ? "positive" : "negative" %>">
        <span class="card-label">Total P&amp;L</span>
        <span class="card-value">
            &#8377; <%= String.format("%,.2f", pl) %>
            <small>(<%= String.format("%.2f", plPercent) %>%)</small>
        </span>
    </div>
</section>

<section class="panel">
    <div class="panel-head">
        <h2>My Holdings</h2>
        <a class="btn btn-outline" href="<%= request.getContextPath() %>/market">Browse Market</a>
    </div>

    <% if (holdings == null || holdings.isEmpty()) { %>
        <div class="empty">
            <p>No holdings yet. Start investing from the Market page.</p>
        </div>
    <% } else { %>
    <table class="table">
        <thead>
            <tr>
                <th>Symbol</th>
                <th>Company</th>
                <th class="num">Qty</th>
                <th class="num">Avg Price</th>
                <th class="num">Current</th>
                <th class="num">Invested</th>
                <th class="num">Value</th>
                <th class="num">P&amp;L</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% for (Holding h : holdings) { %>
            <tr>
                <td><strong><%= h.getSymbol() %></strong></td>
                <td><%= h.getCompanyName() %></td>
                <td class="num"><%= h.getQuantity() %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", h.getAvgBuyPrice()) %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", h.getCurrentPrice()) %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", h.getInvestedValue()) %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", h.getCurrentValue()) %></td>
                <td class="num <%= h.getProfitLoss() >= 0 ? "positive" : "negative" %>">
                    &#8377;<%= String.format("%,.2f", h.getProfitLoss()) %>
                    <small>(<%= String.format("%.2f", h.getProfitLossPercent()) %>%)</small>
                </td>
                <td>
                    <a class="btn btn-sm btn-success" href="<%= request.getContextPath() %>/trade?stockId=<%= h.getStockId() %>&action=BUY">Buy</a>
                    <a class="btn btn-sm btn-danger" href="<%= request.getContextPath() %>/trade?stockId=<%= h.getStockId() %>&action=SELL">Sell</a>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } %>
</section>

<section class="panel">
    <div class="panel-head">
        <h2>Market Watch</h2>
        <input type="text" id="liveSearch" class="search-box" placeholder="Filter stocks..." onkeyup="liveFilter('liveSearch','marketWatchBody')">
    </div>
    <table class="table">
        <thead>
            <tr>
                <th>Symbol</th>
                <th>Company</th>
                <th>Sector</th>
                <th class="num">Price</th>
                <th class="num">Change</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody id="marketWatchBody">
            <% for (Stock s : topStocks) {
                String cls = s.getChange() >= 0 ? "positive" : "negative";
            %>
            <tr>
                <td><strong><%= s.getSymbol() %></strong></td>
                <td><%= s.getCompanyName() %></td>
                <td><%= s.getSector() %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", s.getCurrentPrice()) %></td>
                <td class="num <%= cls %>">
                    <%= String.format("%+.2f", s.getChange()) %> (<%= String.format("%+.2f", s.getChangePercent()) %>%)
                </td>
                <td>
                    <a class="btn btn-sm btn-success" href="<%= request.getContextPath() %>/trade?stockId=<%= s.getStockId() %>&action=BUY">Buy</a>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
</section>

<jsp:include page="footer.jsp" />
