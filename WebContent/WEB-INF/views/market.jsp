<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.pms.model.Stock"%>
<% request.setAttribute("pageTitle", "Market - PMS"); %>
<jsp:include page="header.jsp" />

<%
    List<Stock> stocks = (List<Stock>) request.getAttribute("stocks");
    String q = (String) request.getAttribute("searchQuery");
%>

<section class="page-head">
    <h1>Market</h1>
    <p class="muted">Live prices across all listed stocks</p>
</section>

<section class="panel">
    <form method="get" action="<%= request.getContextPath() %>/market" class="search-form">
        <input type="text" name="search" placeholder="Search by symbol or company..."
               value="<%= q != null ? q : "" %>">
        <button type="submit" class="btn btn-primary">Search</button>
        <button type="button" class="btn btn-outline" onclick="loadStocksAjax()">Refresh (AJAX)</button>
    </form>

    <div id="ajaxStatus" class="muted small"></div>

    <table class="table" id="marketTable">
        <thead>
            <tr>
                <th>Symbol</th>
                <th>Company</th>
                <th>Sector</th>
                <th class="num">Price</th>
                <th class="num">Previous Close</th>
                <th class="num">Change</th>
                <th>Market Cap</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody id="marketBody">
            <% if (stocks == null || stocks.isEmpty()) { %>
                <tr><td colspan="8" class="empty">No stocks match your search.</td></tr>
            <% } else for (Stock s : stocks) {
                String cls = s.getChange() >= 0 ? "positive" : "negative";
            %>
            <tr>
                <td><strong><%= s.getSymbol() %></strong></td>
                <td><%= s.getCompanyName() %></td>
                <td><%= s.getSector() %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", s.getCurrentPrice()) %></td>
                <td class="num">&#8377;<%= String.format("%,.2f", s.getPreviousClose()) %></td>
                <td class="num <%= cls %>">
                    <%= String.format("%+.2f", s.getChange()) %> (<%= String.format("%+.2f", s.getChangePercent()) %>%)
                </td>
                <td><%= s.getMarketCap() %></td>
                <td>
                    <a class="btn btn-sm btn-success" href="<%= request.getContextPath() %>/trade?stockId=<%= s.getStockId() %>&action=BUY">Buy</a>
                    <a class="btn btn-sm btn-danger" href="<%= request.getContextPath() %>/trade?stockId=<%= s.getStockId() %>&action=SELL">Sell</a>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
</section>

<jsp:include page="footer.jsp" />
