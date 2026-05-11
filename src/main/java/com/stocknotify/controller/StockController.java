package com.stocknotify.controller;

import com.stocknotify.config.DiscordConfig;
import com.stocknotify.model.StockPriceData;
import com.stocknotify.service.StockPriceMonitorService;
import com.stocknotify.service.discord.DiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockPriceMonitorService monitorService;
    private final DiscordClient discordClient;
    private final DiscordConfig discordConfig;

    @PostMapping("/{symbol}/prices")
    public ResponseEntity<Map<String, Object>> addPrice(@PathVariable String symbol, @RequestBody Map<String, Double> body) {
        double price = body.get("price");
        monitorService.addToHistory(symbol, price);
        return ResponseEntity.ok(Map.of("symbol", symbol, "price", price, "status", "added"));
    }

    @PostMapping("/{symbol}/check")
    public ResponseEntity<?> checkPrice(@PathVariable String symbol) {
        StockPriceData result = monitorService.analyzePriceDrop(symbol);
        if (result == null) {
            return ResponseEntity.ok(Map.of("symbol", symbol, "message", "Need at least 2 price points in history", "shouldNotify", false));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{symbol}/compare")
    public ResponseEntity<StockPriceData> comparePrices(@PathVariable String symbol, @RequestBody Map<String, Double> body) {
        double currentPrice = body.get("currentPrice");
        double priceOneHourAgo = body.get("priceOneHourAgo");
        StockPriceData result = new StockPriceData(symbol, currentPrice, priceOneHourAgo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/discord/test")
    public ResponseEntity<Map<String, Object>> testDiscord() {
        discordClient.sendDirectMessage(discordConfig.getUserId(),
            ":bell: **Stock Notify - Test Message**\n\n" +
            "If you see this, Discord notifications are working correctly!\n" +
            "Timestamp: " + java.time.LocalDateTime.now()
        );
        return ResponseEntity.ok(Map.of("status", "sent", "userId", discordConfig.getUserId()));
    }
}
