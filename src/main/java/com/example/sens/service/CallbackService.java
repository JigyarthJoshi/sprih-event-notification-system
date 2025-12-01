package com.example.sens.service;

import com.example.sens.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CallbackService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendCallback(Event event, boolean success, String errorMessage) {

        Map<String, Object> body = new HashMap<>();
        body.put("eventId", event.getEventId());
        body.put("status", success ? "COMPLETED" : "FAILED");
        body.put("eventType", event.getEventType().name());
        body.put("processedAt", Instant.now().toString());

        if (!success) {
            log.warn("Failed to send notification " + "eventId = " + event.getEventId() + " eventType = " + event.getEventType().name());
            body.put("errorMessage", errorMessage);
        } else {
            log.info("Successfully sent notification " + "eventId = " + event.getEventId() + " eventType = " + event.getEventType().name());
        }

        try {
            restTemplate.postForEntity(event.getCallbackUrl(), new HttpEntity<>(body), String.class);
            log.info("Callback sent for event {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to send callback", e);
        }
    }
}
