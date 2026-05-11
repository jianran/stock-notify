package com.stocknotify.service.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocknotify.config.DiscordConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class DiscordClient {

    private final DiscordConfig discordConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DiscordClient(DiscordConfig discordConfig) {
        this.discordConfig = discordConfig;
    }

    public void sendDirectMessage(String userId, String message) {
        try {
            String channelUrl = "https://discord.com/api/v10/users/" + userId + "/channels";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bot " + discordConfig.getBotToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = Map.of(
                "recipient_id", userId
            );
            String jsonPayload = objectMapper.writeValueAsString(payload);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            @SuppressWarnings("unchecked")
            Map<String, Object> channelResponse = restTemplate.postForObject(channelUrl, request, Map.class);
            String channelId = (String) channelResponse.get("id");

            // Send message to the channel
            String messageUrl = "https://discord.com/api/v10/channels/" + channelId + "/messages";
            Map<String, String> messagePayload = Map.of("content", message);

            HttpHeaders messageHeaders = new HttpHeaders();
            messageHeaders.set("Authorization", "Bot " + discordConfig.getBotToken());
            messageHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> messageRequest = new HttpEntity<>(messagePayload, messageHeaders);
            restTemplate.postForObject(messageUrl, messageRequest, String.class);

            log.info("Successfully sent Discord DM to user {}", userId);
        } catch (Exception e) {
            log.error("Failed to send Discord DM", e);
        }
    }
}
