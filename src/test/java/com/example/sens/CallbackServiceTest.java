package com.example.sens;

import com.example.sens.model.Event;
import com.example.sens.model.EventType;
import com.example.sens.service.CallbackService;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CallbackServiceTest {

    @Test
    void testCallbackInvokedSuccessfully() {
        CallbackService callbackService = Mockito.spy(new CallbackService());

        Event event = new Event();
        event.setEventId("1234");
        event.setEventType(EventType.PUSH);
        event.setCallbackUrl("https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40");

        callbackService.sendCallback(event, true, null);

        Mockito.verify(callbackService, times(1))
                .sendCallback(event, true, null);
    }
}
