package com.pms.controller;

import com.pms.dao.PortfolioDAO;
import com.pms.model.Transaction;
import com.pms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {

    private PortfolioDAO portfolioDAO;

    @Override
    public void init() throws ServletException {
        portfolioDAO = new PortfolioDAO();
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
        List<Transaction> transactions = portfolioDAO.getTransactionsByUser(user.getUserId());
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/WEB-INF/views/transactions.jsp").forward(request, response);
    }
}
