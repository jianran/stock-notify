package com.stocknotify.service;

import com.stocknotify.config.DiscordConfig;
import com.stocknotify.config.StockConfig;
import com.stocknotify.model.AlphaVantageResponse;
import com.stocknotify.model.StockPriceData;
import com.stocknotify.service.discord.DiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockPriceMonitorService {

    private final StockConfig stockConfig;
    private final DiscordConfig discordConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final DiscordClient discordClient;

    // Map to store historical prices: {symbol -> List of (timestamp, price)}
    private final Map<String, List<PricePoint>> priceHistory = new HashMap<>();

    @Scheduled(fixedRateString = "#{stockConfig.checkIntervalMinutes * 60000}")
    public void checkStockPrices() {
        log.info("Starting stock price check at {}", LocalDateTime.now());

        for (String symbol : stockConfig.getSymbols()) {
            try {
                AlphaVantageResponse response = getStockPrice(symbol);
                if (response != null && response.getGlobalQuote() != null) {
                    double currentPrice = Double.parseDouble(response.getGlobalQuote().getPrice());
                    addToHistory(symbol, currentPrice);

                    StockPriceData priceData = analyzePriceDrop(symbol);
                    if (priceData != null && priceData.isShouldNotify()) {
                        sendDiscordNotification(priceData);
                    }
                }
            } catch (Exception e) {
                log.error("Error checking stock {}", symbol, e);
            }
        }
    }

    private AlphaVantageResponse getStockPrice(String symbol) {
        String url = String.format(
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
            symbol, stockConfig.getAlphaVantageApiKey()
        );
        return restTemplate.getForObject(url, AlphaVantageResponse.class);
    }

    private void addToHistory(String symbol, double price) {
        priceHistory.computeIfAbsent(symbol, k -> new ArrayList<>());
        priceHistory.get(symbol).add(new PricePoint(LocalDateTime.now(), price));

        // Clean up old entries (keep only last 2 hours)
        List<PricePoint> history = priceHistory.get(symbol);
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        history.removeIf(point -> point.timestamp().isBefore(twoHoursAgo));
    }

    private StockPriceData analyzePriceDrop(String symbol) {
        List<PricePoint> history = priceHistory.get(symbol);
        if (history.size() < 2) return null;

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(stockConfig.getHistoryWindowHours());
        double currentPrice = history.get(history.size() - 1).price();

        // Find price closest to 1 hour ago
        double priceOneHourAgo = 0;
        PricePoint closestPoint = null;
        long minDiff = Long.MAX_VALUE;

        for (PricePoint point : history) {
            long diff = Math.abs(point.timestamp().toInstant().toEpochMilli() - oneHourAgo.toInstant().toEpochMilli());
            if (diff < minDiff) {
                minDiff = diff;
                priceOneHourAgo = point.price();
                closestPoint = point;
            }
        }

        return new StockPriceData(symbol, currentPrice, priceOneHourAgo);
    }

    private void sendDiscordNotification(StockPriceData priceData) {
        String message = String.format(
            "⚠️ **Stock Alert: %s**\n\n" +
            "Current Price: $%.2f\n" +
            "1 Hour Ago: $%.2f\n" +
            "Price Drop: %.2f%%\n" +
            "Threshold: 3%%",
            priceData.getSymbol(),
            priceData.getCurrentPrice(),
            priceData.getPriceOneHourAgo(),
            priceData.getPercentChange()
        );

        discordClient.sendDirectMessage(discordConfig.getUserId(), message);
        log.info("Sent Discord notification for stock {}", priceData.getSymbol());
    }

    private record PricePoint(LocalDateTime timestamp, double price) {}
}
