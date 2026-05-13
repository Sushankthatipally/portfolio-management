package com.pms.model;

import java.io.Serializable;

public class Holding implements Serializable {
    private int holdingId;
    private int userId;
    private int stockId;
    private String symbol;
    private String companyName;
    private int quantity;
    private double avgBuyPrice;
    private double currentPrice;

    public Holding() {}

    public int getHoldingId() { return holdingId; }
    public void setHoldingId(int holdingId) { this.holdingId = holdingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getStockId() { return stockId; }
    public void setStockId(int stockId) { this.stockId = stockId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getAvgBuyPrice() { return avgBuyPrice; }
    public void setAvgBuyPrice(double avgBuyPrice) { this.avgBuyPrice = avgBuyPrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public double getInvestedValue() {
        return quantity * avgBuyPrice;
    }

    public double getCurrentValue() {
        return quantity * currentPrice;
    }

    public double getProfitLoss() {
        return getCurrentValue() - getInvestedValue();
    }

    public double getProfitLossPercent() {
        if (getInvestedValue() == 0) return 0;
        return (getProfitLoss() / getInvestedValue()) * 100;
    }
}
