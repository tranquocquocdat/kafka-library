// kafka-strategy/src/main/java/com/example/kafka/strategy/UserCreatedStrategy.java
package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.UserEvent;
import org.springframework.stereotype.Component;

@Component
@org.dat.tran.kafka.annotation.EventStrategy(EventType.USER_CREATED)
public class UserCreatedStrategy implements EventStrategy<UserEvent> {
    @Override
    public void process(UserEvent event) {
        System.out.println("Processing USER_CREATED event: " + event);
        // add business logic here
    }
}
