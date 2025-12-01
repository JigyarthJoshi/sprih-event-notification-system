package com.example.sens;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testValidEventSubmission() throws Exception {
        Map<String, Object> request = Map.of(
                "eventType", "EMAIL",
                "payload", Map.of("msg", "hello"),
                "callbackUrl", "https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40"
        );

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists());
    }

    @Test
    void testInvalidEventType() throws Exception {
        Map<String, Object> request = Map.of(
                "eventType", "INVALID",
                "payload", Map.of("msg", "hello"),
                "callbackUrl", "https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40"
        );

        mockMvc.perform(post("/api/events")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingPayloadField() throws Exception {
        Map<String, Object> request = Map.of(
                "eventType", "SMS",
                "callbackUrl", "https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40"
        );

        mockMvc.perform(post("/api/events")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingCallbackUrl() throws Exception {
        Map<String, Object> request = Map.of(
                "eventType", "SMS",
                "payload", Map.of("msg", "hello")
        );

        mockMvc.perform(post("/api/events")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
