package org.dat.tran.kafka.model;

import java.util.Map;
import java.util.Set;

public enum Topic {
    USER_EVENT("user-topic", Set.of(EventType.USER_CREATED, EventType.USER_DELETED)),
    ORDER_EVENT("order-topic", Set.of(EventType.ORDER_CREATED, EventType.ORDER_CANCELLED)),
    PAYMENT_EVENT("payment-topic", Set.of(EventType.PAYMENT_SUCCESS, EventType.PAYMENT_FAILED));

    private final String topicName;
    private final Set<EventType> eventTypes;

    Topic(String topicName, Set<EventType> eventTypes) {
        this.topicName = topicName;
        this.eventTypes = eventTypes;
    }

    public String getTopicName() {
        return topicName;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    private static final Map<EventType, Topic> EVENT_TYPE_TO_TOPIC = Map.of(
            EventType.USER_CREATED, USER_EVENT,
            EventType.USER_DELETED, USER_EVENT,
            EventType.ORDER_CREATED, ORDER_EVENT,
            EventType.ORDER_CANCELLED, ORDER_EVENT,
            EventType.PAYMENT_SUCCESS, PAYMENT_EVENT,
            EventType.PAYMENT_FAILED, PAYMENT_EVENT
    );

    public static Topic fromEventType(EventType eventType) {
        return EVENT_TYPE_TO_TOPIC.get(eventType);
    }

    public boolean supports(EventType eventType) {
        return false;
    }
}
