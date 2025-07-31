// kafka-app/src/main/java/com/example/kafka/app/TestDataRunner.java
package org.dat.tran.kafka.app;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.UserEvent;
import org.dat.tran.kafka.producer.OrderEventProducer;
import org.dat.tran.kafka.producer.UserEventProducer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sends test events after application startup.
 */
@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    private UserEventProducer userEventProducer;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Send a user created event
        UserEvent userEvent = new UserEvent("u123", "alice", EventType.USER_CREATED);
        userEventProducer.send(userEvent);

        // Send an order created event
        OrderEvent orderEvent = new OrderEvent("o456", 99.99, EventType.ORDER_CREATED);
        orderEventProducer.send(orderEvent);
    }
}
