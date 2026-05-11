package com.stocknotify.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockPriceData {
    private String symbol;
    private double currentPrice;
    private double priceOneHourAgo;
    private LocalDateTime timestamp;
    private double percentChange;
    private boolean shouldNotify;

    public StockPriceData(String symbol, double currentPrice, double priceOneHourAgo) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceOneHourAgo = priceOneHourAgo;
        this.timestamp = LocalDateTime.now();
        this.percentChange = calculatePercentChange();
        this.shouldNotify = calculateShouldNotify();
    }

    private double calculatePercentChange() {
        if (priceOneHourAgo == 0) return 0;
        return ((priceOneHourAgo - currentPrice) / priceOneHourAgo) * 100;
    }

    private boolean calculateShouldNotify() {
        return percentChange >= 3.0;
    }
}
