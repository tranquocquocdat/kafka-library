// kafka-handler/src/main/java/com/example/kafka/handler/UserEventHandler.java
package org.dat.tran.kafka.handler;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.dat.tran.kafka.annotation.TopicHandler;
import org.dat.tran.kafka.model.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles messages for the "user-topic". Parses JSON into UserEvent and
 * delegates to the EventStrategy matching the event type.
 */
@Component
@TopicHandler(topic = "user-topic")
public class UserEventHandler implements KafkaMessageHandler {

    @Autowired
    private ObjectMapper objectMapper;  // for JSON deserialization

    @Autowired
    private List<EventStrategy<?>> strategies;  // all strategies

    @Override
    public void handle(KafkaMessage<?> message) {
        try {
            // Parse payload (JSON) into UserEvent
            String json = (String) message.getPayload();
            UserEvent event = objectMapper.readValue(json, UserEvent.class);
            System.out.println("UserEventHandler received: " + event);
            // Find and invoke the matching strategy
            for (EventStrategy<?> strat : strategies) {
                org.dat.tran.kafka.annotation.EventStrategy annotation = strat.getClass().getAnnotation(org.dat.tran.kafka.annotation.EventStrategy.class);
                if (annotation != null && annotation.value() == event.getEventType()) {
                    // Safe cast since types match by annotation
                    @SuppressWarnings("unchecked")
                    EventStrategy<UserEvent> strategy = (EventStrategy<UserEvent>) strat;
                    strategy.process(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
