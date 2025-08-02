package org.dat.tran.kafka.registry;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.registry.anotation.HandelEventStrategy;

import java.util.Objects;

/**
 * Composite key for Topic + EventType combination
 * This is the KEY INNOVATION - single key instead of nested maps!
 */
public class TopicEventKey {
    private final Topic topic;
    private final EventType eventType;
    
    public TopicEventKey(Topic topic, EventType eventType) {
        this.topic = Objects.requireNonNull(topic, "Topic cannot be null");
        this.eventType = Objects.requireNonNull(eventType, "EventType cannot be null");
    }
    
    // Getters
    public Topic getTopic() {
        return topic;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public String getTopicName() {
        return topic.getTopicName();
    }
    
    // Important: proper equals and hashCode for Map key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicEventKey that = (TopicEventKey) o;
        return topic == that.topic && eventType == that.eventType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(topic, eventType);
    }
    
    @Override
    public String toString() {
        return topic.name() + "." + eventType.name();
    }
    
    // Factory methods for easy creation
    public static TopicEventKey of(Topic topic, EventType eventType) {
        return new TopicEventKey(topic, eventType);
    }
    
    public static TopicEventKey from(HandelEventStrategy annotation) {
        return new TopicEventKey(annotation.topic(), annotation.eventType());
    }
}
