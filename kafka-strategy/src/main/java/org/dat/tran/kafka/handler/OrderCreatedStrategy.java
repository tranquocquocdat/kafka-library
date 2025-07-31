// kafka-strategy/src/main/java/com/example/kafka/strategy/OrderCreatedStrategy.java
package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.springframework.stereotype.Component;

@Component
@org.dat.tran.kafka.annotation.EventStrategy(EventType.ORDER_CREATED)
public class OrderCreatedStrategy implements EventStrategy<OrderEvent> {
    @Override
    public void process(OrderEvent event) {
        System.out.println("Processing ORDER_CREATED event: " + event);
        // add business logic here
    }
}
