package com.example.sens;

import com.example.sens.controller.EventController;
import com.example.sens.service.CallbackService;
import com.example.sens.service.EventDispatcherService;
import com.example.sens.worker.EventWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@SpringBootApplication
@RequiredArgsConstructor
public class EventNotificationSystemApplication {

    private Thread emailThread;
    private Thread smsThread;
    private Thread pushThread;

    private EventWorker emailWorker;
    private EventWorker smsWorker;
    private EventWorker pushWorker;

    private final EventDispatcherService dispatcher;
    private final CallbackService callbackService;
    private final EventController controller;

    public static void main(String[] args) {
        SpringApplication.run(EventNotificationSystemApplication.class, args);
    }

    @PostConstruct
    public void startWorkers() {

        emailWorker = new EventWorker(dispatcher.getEmailQueue(), callbackService, 5);
        smsWorker = new EventWorker(dispatcher.getSmsQueue(), callbackService, 3);
        pushWorker = new EventWorker(dispatcher.getPushQueue(), callbackService, 2);

        emailThread = new Thread(emailWorker);
        smsThread = new Thread(smsWorker);
        pushThread = new Thread(pushWorker);

        emailThread.start();
        smsThread.start();
        pushThread.start();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {

        controller.stopAcceptingEvents();

        emailWorker.stop();
        smsWorker.stop();
        pushWorker.stop();

        emailThread.join();
        smsThread.join();
        pushThread.join();
    }
}
