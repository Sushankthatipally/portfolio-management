package com.pms.dao;

import com.pms.model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public List<Stock> getAllStocks() {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stocks ORDER BY symbol";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractStock(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Stock getById(int stockId) {
        String sql = "SELECT * FROM stocks WHERE stock_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stockId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractStock(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stock getBySymbol(String symbol) {
        String sql = "SELECT * FROM stocks WHERE symbol = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, symbol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractStock(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Stock> searchStocks(String query) {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stocks WHERE symbol LIKE ? OR company_name LIKE ? ORDER BY symbol";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Stock extractStock(ResultSet rs) throws SQLException {
        Stock s = new Stock();
        s.setStockId(rs.getInt("stock_id"));
        s.setSymbol(rs.getString("symbol"));
        s.setCompanyName(rs.getString("company_name"));
        s.setSector(rs.getString("sector"));
        s.setCurrentPrice(rs.getDouble("current_price"));
        s.setPreviousClose(rs.getDouble("previous_close"));
        s.setMarketCap(rs.getString("market_cap"));
        return s;
    }
}
