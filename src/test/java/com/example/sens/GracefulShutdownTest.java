package com.example.sens;

import com.example.sens.model.Event;
import com.example.sens.model.EventType;
import com.example.sens.service.CallbackService;
import com.example.sens.worker.EventWorker;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class GracefulShutdownTest {

    @Test
    void testWorkerFinishesJobsBeforeShutdown() throws Exception {
        var queue = new LinkedBlockingQueue<Event>();

        Event event = new Event();
        event.setEventId("101");
        event.setEventType(EventType.PUSH);
        event.setCallbackUrl("https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40");

        queue.add(event);

        CallbackService callback = org.mockito.Mockito.mock(CallbackService.class);
        EventWorker worker = new EventWorker(queue, callback, 1);

        Thread t = new Thread(worker);
        t.start();

        worker.stop();

        t.join(2000);

        assertFalse(t.isAlive());
        org.mockito.Mockito.verify(callback).sendCallback(event, true, null);
    }
}
