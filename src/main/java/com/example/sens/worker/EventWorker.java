package com.example.sens.worker;

import com.example.sens.model.Event;
import com.example.sens.service.CallbackService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventWorker implements Runnable {

    private final BlockingQueue<Event> queue;
    private final CallbackService callbackService;
    private final int delaySeconds;
    private volatile boolean running = true;

    public EventWorker(BlockingQueue<Event> queue, CallbackService callbackService, int delaySeconds) {
        this.queue = queue;
        this.callbackService = callbackService;
        this.delaySeconds = delaySeconds;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running || !queue.isEmpty()) {
            try {
                Event event = queue.poll(500, TimeUnit.MILLISECONDS); 

                if (event != null) {
                    Thread.sleep(delaySeconds * 1000L);

                    boolean fail = ThreadLocalRandom.current().nextInt(10) == 0;
                    if (fail) {
                        callbackService.sendCallback(event, false, "Simulated processing failure");
                    } else {
                        callbackService.sendCallback(event, true, null);
                    }
                }
            } catch (Exception e) {
                log.error("Worker error", e);
            }
        }
    }
}
