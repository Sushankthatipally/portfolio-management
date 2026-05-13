package com.pms.model;

import java.io.Serializable;

public class Stock implements Serializable {
    private int stockId;
    private String symbol;
    private String companyName;
    private String sector;
    private double currentPrice;
    private double previousClose;
    private String marketCap;

    public Stock() {}

    public int getStockId() { return stockId; }
    public void setStockId(int stockId) { this.stockId = stockId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public double getPreviousClose() { return previousClose; }
    public void setPreviousClose(double previousClose) { this.previousClose = previousClose; }

    public String getMarketCap() { return marketCap; }
    public void setMarketCap(String marketCap) { this.marketCap = marketCap; }

    public double getChange() {
        return currentPrice - previousClose;
    }

    public double getChangePercent() {
        if (previousClose == 0) return 0;
        return ((currentPrice - previousClose) / previousClose) * 100;
    }
}
