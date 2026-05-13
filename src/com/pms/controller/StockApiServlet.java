package com.pms.controller;

import com.pms.dao.StockDAO;
import com.pms.model.Stock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/stocks")
public class StockApiServlet extends HttpServlet {

    private StockDAO stockDAO;

    @Override
    public void init() throws ServletException {
        stockDAO = new StockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String format = request.getParameter("format");
        String query = request.getParameter("q");

        List<Stock> stocks = (query != null && !query.trim().isEmpty())
                ? stockDAO.searchStocks(query.trim())
                : stockDAO.getAllStocks();

        if ("json".equalsIgnoreCase(format)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < stocks.size(); i++) {
                Stock s = stocks.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"stockId\":").append(s.getStockId()).append(",")
                  .append("\"symbol\":\"").append(s.getSymbol()).append("\",")
                  .append("\"companyName\":\"").append(escape(s.getCompanyName())).append("\",")
                  .append("\"sector\":\"").append(s.getSector()).append("\",")
                  .append("\"currentPrice\":").append(s.getCurrentPrice()).append(",")
                  .append("\"previousClose\":").append(s.getPreviousClose()).append(",")
                  .append("\"change\":").append(s.getChange()).append(",")
                  .append("\"changePercent\":").append(String.format("%.2f", s.getChangePercent()))
                  .append("}");
            }
            sb.append("]");
            out.print(sb.toString());
            out.flush();
        } else {
            response.setContentType("application/xml");
            PrintWriter out = response.getWriter();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<stocks>");
            for (Stock s : stocks) {
                out.println("  <stock>");
                out.println("    <stockId>" + s.getStockId() + "</stockId>");
                out.println("    <symbol>" + s.getSymbol() + "</symbol>");
                out.println("    <companyName>" + escape(s.getCompanyName()) + "</companyName>");
                out.println("    <sector>" + s.getSector() + "</sector>");
                out.println("    <currentPrice>" + s.getCurrentPrice() + "</currentPrice>");
                out.println("    <previousClose>" + s.getPreviousClose() + "</previousClose>");
                out.println("    <change>" + String.format("%.2f", s.getChange()) + "</change>");
                out.println("    <changePercent>" + String.format("%.2f", s.getChangePercent()) + "</changePercent>");
                out.println("  </stock>");
            }
            out.println("</stocks>");
            out.flush();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
