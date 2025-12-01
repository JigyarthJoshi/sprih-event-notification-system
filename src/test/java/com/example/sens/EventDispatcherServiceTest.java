package com.example.sens;

import com.example.sens.model.Event;
import com.example.sens.model.EventType;
import com.example.sens.service.EventDispatcherService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EventDispatcherServiceTest {

    @Test
    void testEventRoutingToCorrectQueue() {
        EventDispatcherService dispatcher = new EventDispatcherService();

        Event emailEvent = new Event();
        emailEvent.setEventId("1");
        emailEvent.setEventType(EventType.EMAIL);
        emailEvent.setPayload(Map.of("a", "b"));
        emailEvent.setCallbackUrl("https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40");

        dispatcher.dispatch(emailEvent);

        assertEquals(1, dispatcher.getEmailQueue().size());
        assertEquals(0, dispatcher.getSmsQueue().size());
        assertEquals(0, dispatcher.getPushQueue().size());
    }

    @Test
    void testFIFOOrder() throws Exception {
        EventDispatcherService dispatcher = new EventDispatcherService();

        Event e1 = new Event(); e1.setEventId("E1"); e1.setEventType(EventType.SMS);
        Event e2 = new Event(); e2.setEventId("E2"); e2.setEventType(EventType.SMS);

        dispatcher.dispatch(e1);
        dispatcher.dispatch(e2);

        assertEquals("E1", dispatcher.getSmsQueue().take().getEventId());
        assertEquals("E2", dispatcher.getSmsQueue().take().getEventId());
    }
}