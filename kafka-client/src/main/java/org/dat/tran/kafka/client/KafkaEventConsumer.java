package org.dat.tran.kafka.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.model.UserEvent;
import org.dat.tran.kafka.registry.EventStrategyRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumers that listen to topics and delegate to registry
 * This is where the magic happens - automatic routing to strategies!
 */
@Component
public class KafkaEventConsumer {

    @Autowired
    private EventStrategyRegistry registry;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Listen to user-topic and process USER events
     */
    @KafkaListener(topics = "user-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
        System.out.println("📨 Received USER event from " + topic);
        
        try {
            // Parse event type from JSON
            EventType eventType = extractEventType(message);
            
            // Parse into UserEvent object
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            
            // 🎯 Single call to registry - automatic strategy execution!
            registry.processEvent(Topic.USER_EVENT, eventType, event);
            
        } catch (Exception e) {
            System.err.println("❌ Error processing user event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Listen to order-topic and process ORDER events
     */
    @KafkaListener(topics = "order-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
        System.out.println("📨 Received ORDER event from " + topic);
        
        try {
            // Parse event type from JSON
            EventType eventType = extractEventType(message);
            
            // Parse into OrderEvent object
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
            
            // 🎯 Single call to registry - automatic strategy execution!
            registry.processEvent(Topic.ORDER_EVENT, eventType, event);
            
        } catch (Exception e) {
            System.err.println("❌ Error processing order event: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Extract event type from JSON message
     */
    private EventType extractEventType(String message) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(message);
        String eventTypeStr = jsonNode.get("eventType").asText();
        return EventType.valueOf(eventTypeStr);
    }
}