package com.pms.controller;

import com.pms.dao.StockDAO;
import com.pms.model.Stock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/market")
public class MarketServlet extends HttpServlet {

    private StockDAO stockDAO;

    @Override
    public void init() throws ServletException {
        stockDAO = new StockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String search = request.getParameter("search");
        List<Stock> stocks;
        if (search != null && !search.trim().isEmpty()) {
            stocks = stockDAO.searchStocks(search.trim());
            request.setAttribute("searchQuery", search);
        } else {
            stocks = stockDAO.getAllStocks();
        }
        request.setAttribute("stocks", stocks);
        request.getRequestDispatcher("/WEB-INF/views/market.jsp").forward(request, response);
    }
}
