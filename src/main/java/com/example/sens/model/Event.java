package com.example.sens.model;

import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
public class Event {
    private String eventId;
    private EventType eventType;
    private Map<String, Object> payload;
    private String callbackUrl;
    private Instant createdAt = Instant.now();
}