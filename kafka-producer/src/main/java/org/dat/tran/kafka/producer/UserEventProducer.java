// kafka-producer/src/main/java/com/example/kafka/producer/UserEventProducer.java
package org.dat.tran.kafka.producer;


import org.dat.tran.kafka.model.UserEvent;
import org.springframework.stereotype.Component;

/**
 * Producer for UserEvent messages to "user-topic".
 */
@Component
public class UserEventProducer extends AbstractKafkaProducer<UserEvent> {
    public UserEventProducer() {
        super("user-topic");
    }
}
