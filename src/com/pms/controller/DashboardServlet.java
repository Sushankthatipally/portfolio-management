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
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private PortfolioDAO portfolioDAO;
    private StockDAO stockDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        portfolioDAO = new PortfolioDAO();
        stockDAO = new StockDAO();
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
        User fresh = userDAO.getById(user.getUserId());
        if (fresh != null) {
            session.setAttribute("user", fresh);
            user = fresh;
        }

        List<Holding> holdings = portfolioDAO.getHoldingsByUser(user.getUserId());
        List<Stock> topStocks = stockDAO.getAllStocks();

        double investedValue = 0, currentValue = 0;
        for (Holding h : holdings) {
            investedValue += h.getInvestedValue();
            currentValue += h.getCurrentValue();
        }

        request.setAttribute("holdings", holdings);
        request.setAttribute("topStocks", topStocks);
        request.setAttribute("investedValue", investedValue);
        request.setAttribute("currentValue", currentValue);
        request.setAttribute("totalPL", currentValue - investedValue);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
