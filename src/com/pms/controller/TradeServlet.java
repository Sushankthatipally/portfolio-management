package com.pms.controller;

import com.pms.dao.PortfolioDAO;
import com.pms.dao.StockDAO;
import com.pms.dao.UserDAO;
import com.pms.model.Holding;
import com.pms.model.Stock;
import com.pms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/trade")
public class TradeServlet extends HttpServlet {

    private StockDAO stockDAO;
    private PortfolioDAO portfolioDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        stockDAO = new StockDAO();
        portfolioDAO = new PortfolioDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        String stockIdParam = request.getParameter("stockId");
        String action = request.getParameter("action");
        if (stockIdParam == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/market");
            return;
        }
        Stock stock = stockDAO.getById(Integer.parseInt(stockIdParam));
        if (stock == null) {
            response.sendRedirect(request.getContextPath() + "/market");
            return;
        }
        Holding holding = portfolioDAO.getHolding(user.getUserId(), stock.getStockId());
        request.setAttribute("stock", stock);
        request.setAttribute("action", action);
        request.setAttribute("holding", holding);
        request.getRequestDispatcher("/WEB-INF/views/trade.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        try {
            int stockId = Integer.parseInt(request.getParameter("stockId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String action = request.getParameter("action");

            if (quantity <= 0) throw new IllegalArgumentException("Invalid quantity");

            Stock stock = stockDAO.getById(stockId);
            if (stock == null) throw new IllegalArgumentException("Stock not found");

            double totalAmount = stock.getCurrentPrice() * quantity;
            String message;

            if ("BUY".equalsIgnoreCase(action)) {
                if (user.getBalance() < totalAmount) {
                    request.setAttribute("error", "Insufficient balance");
                    request.setAttribute("stock", stock);
                    request.setAttribute("action", action);
                    request.getRequestDispatcher("/WEB-INF/views/trade.jsp").forward(request, response);
                    return;
                }
                if (portfolioDAO.buyStock(user.getUserId(), stockId, quantity, stock.getCurrentPrice())) {
                    message = "Successfully purchased " + quantity + " shares of " + stock.getSymbol();
                } else {
                    message = "Buy order failed";
                }
            } else {
                if (portfolioDAO.sellStock(user.getUserId(), stockId, quantity, stock.getCurrentPrice())) {
                    message = "Successfully sold " + quantity + " shares of " + stock.getSymbol();
                } else {
                    message = "Sell order failed - insufficient shares";
                }
            }

            User refreshed = userDAO.getById(user.getUserId());
            session.setAttribute("user", refreshed);
            session.setAttribute("flashMessage", message);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (Exception e) {
            request.setAttribute("error", "Trade failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/market.jsp").forward(request, response);
        }
    }
}
