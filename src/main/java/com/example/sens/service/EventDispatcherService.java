package com.example.sens.service;

import com.example.sens.model.Event;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class EventDispatcherService {

    @Getter
    private final BlockingQueue<Event> emailQueue = new LinkedBlockingQueue<>();

    @Getter
    private final BlockingQueue<Event> smsQueue = new LinkedBlockingQueue<>();

    @Getter
    private final BlockingQueue<Event> pushQueue = new LinkedBlockingQueue<>();

    public void dispatch(Event event) {
        switch (event.getEventType()) {
            case EMAIL -> emailQueue.add(event);
            case SMS -> smsQueue.add(event);
            case PUSH -> pushQueue.add(event);
            default -> throw new IllegalArgumentException("Unsupported event type");
        }
    }
}
