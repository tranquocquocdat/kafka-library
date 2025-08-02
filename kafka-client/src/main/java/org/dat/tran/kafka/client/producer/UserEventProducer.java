package org.dat.tran.kafka.client.producer;

import org.dat.tran.kafka.client.producer.AbstractKafkaProducer;
import org.dat.tran.kafka.model.UserEvent;
import org.springframework.stereotype.Component;

/**
 * Producer for UserEvent messages to user-topic
 */
@Component
public class UserEventProducer extends AbstractKafkaProducer<UserEvent> {
    public UserEventProducer() {
        super("user-topic");  // Topic.USER_EVENT.getTopicName()
    }
}
