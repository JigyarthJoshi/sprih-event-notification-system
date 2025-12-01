package com.example.sens.controller;

import com.example.sens.model.Event;
import com.example.sens.service.EventDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventDispatcherService dispatcher;
    private boolean acceptingEvents = true;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {

        if (!acceptingEvents) {
            return ResponseEntity.status(503).body("Server shutting down. No new events accepted.");
        }

        if (event.getEventType() == null) {
            return ResponseEntity.badRequest().body("eventType is required");
        }

        if (event.getPayload() == null) {
            return ResponseEntity.badRequest().body("payload is required");
        }

        if (event.getCallbackUrl() == null) {
            return ResponseEntity.badRequest().body("callbackUrl is required");
        }

        event.setEventId(UUID.randomUUID().toString());
        dispatcher.dispatch(event);

        return ResponseEntity.ok(
                Map.of("eventId", event.getEventId(),
                       "message", "Event accepted for processing.")
        );
    }

    public void stopAcceptingEvents() {
        this.acceptingEvents = false;
    }
}
