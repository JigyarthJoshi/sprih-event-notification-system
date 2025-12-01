# Event Notification System

This is a Java Spring Boot–based **Event Notification System** that accepts events via a REST API and processes them asynchronously using separate queues for **EMAIL**, **SMS**, and **PUSH** notifications.

Each event type has its own worker thread, FIFO queue, simulated processing delay, and callback mechanism.

---

## Run the Project

### **Run Locally**

1.  Build the JAR:  ``` mvn clean package ```

2.  Start the application: ```java -jar target/sprih-event-notification-system-0.0.1-SNAPSHOT.jar```

3. API will be available at: ```http://localhost:8080/api/events```

### Run using docker

1. Build the JAR:```mvn clean package -DskipTests```

2. Start with Docker Compose:```docker compose up --build```
Note : If your system uses legacy Docker Compose v1, use:
```docker-compose up --build```

3. Application will run at:
```http://localhost:8080/api/events```

## API usage
Callback url = ```https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40```
It is hosted and responses on every call.

POST **/api/events**

Submit a new event for processing.

Example Request
```json
{
  "eventType": "EMAIL",
  "payload": {
    "recipient": "user@example.com",
    "message": "Notification Email"
  },
  "callbackUrl": "https://webhook.site/d2e574fd-2fde-4b4f-9d60-e398f73cae40"
}
```

Reponse
```json
{
  "eventId": "e123",
  "message": "Event accepted for processing."
}
```

Once the event finishes processing, the system sends a POST callback to the provided URL.

Success Callback
```json
{
  "eventId": "e123",
  "status": "COMPLETED",
  "eventType": "EMAIL",
  "processedAt": "2025-07-01T12:34:56Z"
}
```

Failure Callback
```json
{
  "eventId": "e123",
  "status": "FAILED",
  "eventType": "EMAIL",
  "errorMessage": "Simulated processing failure",
  "processedAt": "2025-07-01T12:34:56Z"
}
```