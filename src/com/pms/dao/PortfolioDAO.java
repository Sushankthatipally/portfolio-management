package com.pms.dao;

import com.pms.model.Holding;
import com.pms.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAO {

    public List<Holding> getHoldingsByUser(int userId) {
        List<Holding> list = new ArrayList<>();
        String sql = "SELECT h.holding_id, h.user_id, h.stock_id, h.quantity, h.avg_buy_price, " +
                     "s.symbol, s.company_name, s.current_price " +
                     "FROM holdings h JOIN stocks s ON h.stock_id = s.stock_id " +
                     "WHERE h.user_id = ? AND h.quantity > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Holding h = new Holding();
                    h.setHoldingId(rs.getInt("holding_id"));
                    h.setUserId(rs.getInt("user_id"));
                    h.setStockId(rs.getInt("stock_id"));
                    h.setQuantity(rs.getInt("quantity"));
                    h.setAvgBuyPrice(rs.getDouble("avg_buy_price"));
                    h.setSymbol(rs.getString("symbol"));
                    h.setCompanyName(rs.getString("company_name"));
                    h.setCurrentPrice(rs.getDouble("current_price"));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Holding getHolding(int userId, int stockId) {
        String sql = "SELECT * FROM holdings WHERE user_id = ? AND stock_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, stockId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Holding h = new Holding();
                    h.setHoldingId(rs.getInt("holding_id"));
                    h.setUserId(rs.getInt("user_id"));
                    h.setStockId(rs.getInt("stock_id"));
                    h.setQuantity(rs.getInt("quantity"));
                    h.setAvgBuyPrice(rs.getDouble("avg_buy_price"));
                    return h;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean buyStock(int userId, int stockId, int quantity, double price) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            Holding existing = getHolding(userId, stockId);
            if (existing == null) {
                String insert = "INSERT INTO holdings (user_id, stock_id, quantity, avg_buy_price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, stockId);
                    ps.setInt(3, quantity);
                    ps.setDouble(4, price);
                    ps.executeUpdate();
                }
            } else {
                int newQty = existing.getQuantity() + quantity;
                double newAvg = ((existing.getQuantity() * existing.getAvgBuyPrice()) + (quantity * price)) / newQty;
                String update = "UPDATE holdings SET quantity = ?, avg_buy_price = ? WHERE holding_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setInt(1, newQty);
                    ps.setDouble(2, newAvg);
                    ps.setInt(3, existing.getHoldingId());
                    ps.executeUpdate();
                }
            }

            String tx = "INSERT INTO transactions (user_id, stock_id, transaction_type, quantity, price, total_amount) VALUES (?, ?, 'BUY', ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(tx)) {
                ps.setInt(1, userId);
                ps.setInt(2, stockId);
                ps.setInt(3, quantity);
                ps.setDouble(4, price);
                ps.setDouble(5, quantity * price);
                ps.executeUpdate();
            }

            String upd = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setDouble(1, quantity * price);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean sellStock(int userId, int stockId, int quantity, double price) {
        Connection conn = null;
        try {
            Holding existing = getHolding(userId, stockId);
            if (existing == null || existing.getQuantity() < quantity) return false;

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int newQty = existing.getQuantity() - quantity;
            String update = "UPDATE holdings SET quantity = ? WHERE holding_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(update)) {
                ps.setInt(1, newQty);
                ps.setInt(2, existing.getHoldingId());
                ps.executeUpdate();
            }

            String tx = "INSERT INTO transactions (user_id, stock_id, transaction_type, quantity, price, total_amount) VALUES (?, ?, 'SELL', ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(tx)) {
                ps.setInt(1, userId);
                ps.setInt(2, stockId);
                ps.setInt(3, quantity);
                ps.setDouble(4, price);
                ps.setDouble(5, quantity * price);
                ps.executeUpdate();
            }

            String upd = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setDouble(1, quantity * price);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT t.*, s.symbol, s.company_name FROM transactions t " +
                     "JOIN stocks s ON t.stock_id = s.stock_id " +
                     "WHERE t.user_id = ? ORDER BY t.transaction_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setTransactionId(rs.getInt("transaction_id"));
                    t.setUserId(rs.getInt("user_id"));
                    t.setStockId(rs.getInt("stock_id"));
                    t.setSymbol(rs.getString("symbol"));
                    t.setCompanyName(rs.getString("company_name"));
                    t.setTransactionType(rs.getString("transaction_type"));
                    t.setQuantity(rs.getInt("quantity"));
                    t.setPrice(rs.getDouble("price"));
                    t.setTotalAmount(rs.getDouble("total_amount"));
                    t.setTransactionDate(rs.getTimestamp("transaction_date"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
