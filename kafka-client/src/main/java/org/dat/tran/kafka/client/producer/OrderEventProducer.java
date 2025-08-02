package org.dat.tran.kafka.client.producer;

import org.dat.tran.kafka.client.producer.AbstractKafkaProducer;
import org.dat.tran.kafka.model.OrderEvent;
import org.springframework.stereotype.Component;

/**
 * Producer for OrderEvent messages to order-topic
 */
@Component
public class OrderEventProducer extends AbstractKafkaProducer<OrderEvent> {
    public OrderEventProducer() {
        super("order-topic");  // Topic.ORDER_EVENT.getTopicName()
    }
}