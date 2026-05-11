package com.stocknotify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "stock")
public class StockConfig {
    private List<String> symbols;
    private String alphaVantageApiKey;
    private double dropThreshold = 3.0;
    private long checkIntervalMinutes = 10;
    private long historyWindowHours = 1;

    // Getters and setters
    public List<String> getSymbols() { return symbols; }
    public void setSymbols(List<String> symbols) { this.symbols = symbols; }
    public String getAlphaVantageApiKey() { return alphaVantageApiKey; }
    public void setAlphaVantageApiKey(String apiKey) { this.alphaVantageApiKey = apiKey; }
    public double getDropThreshold() { return dropThreshold; }
    public void setDropThreshold(double dropThreshold) { this.dropThreshold = dropThreshold; }
    public long getCheckIntervalMinutes() { return checkIntervalMinutes; }
    public void setCheckIntervalMinutes(long checkIntervalMinutes) { this.checkIntervalMinutes = checkIntervalMinutes; }
    public long getHistoryWindowHours() { return historyWindowHours; }
    public void setHistoryWindowHours(long historyWindowHours) { this.historyWindowHours = historyWindowHours; }
}
