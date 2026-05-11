package com.stocknotify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordConfig {
    private String botToken;
    private String userId;

    public String getBotToken() { return botToken; }
    public void setBotToken(String botToken) { this.botToken = botToken; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
