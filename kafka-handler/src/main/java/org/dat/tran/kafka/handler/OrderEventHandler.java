// kafka-handler/src/main/java/com/example/kafka/handler/OrderEventHandler.java
package org.dat.tran.kafka.handler;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.dat.tran.kafka.annotation.TopicHandler;
import org.dat.tran.kafka.model.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@TopicHandler(topic = "order-topic")
public class OrderEventHandler implements KafkaMessageHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private List<EventStrategy<?>> strategies;

    @Override
    public void handle(KafkaMessage<?> message) {
        try {
            String json = (String) message.getPayload();
            OrderEvent event = objectMapper.readValue(json, OrderEvent.class);
            System.out.println("OrderEventHandler received: " + event);
            for (EventStrategy<?> strat : strategies) {
                org.dat.tran.kafka.annotation.EventStrategy annotation = strat.getClass().getAnnotation(org.dat.tran.kafka.annotation.EventStrategy.class);
                if (annotation != null && annotation.value() == event.getEventType()) {
                    @SuppressWarnings("unchecked")
                    EventStrategy<OrderEvent> strategy = (EventStrategy<OrderEvent>) strat;
                    strategy.process(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
