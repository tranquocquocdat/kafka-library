// kafka-producer/src/main/java/com/example/kafka/producer/OrderEventProducer.java
package org.dat.tran.kafka.producer;

import org.dat.tran.kafka.model.OrderEvent;
import org.springframework.stereotype.Component;

/**
 * Producer for OrderEvent messages to "order-topic".
 */
@Component
public class OrderEventProducer extends AbstractKafkaProducer<OrderEvent> {
    public OrderEventProducer() {
        super("order-topic");
    }
}
