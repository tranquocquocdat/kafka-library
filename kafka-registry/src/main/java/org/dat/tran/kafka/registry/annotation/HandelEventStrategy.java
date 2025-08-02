package org.dat.tran.kafka.registry.annotation;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.Topic;

import java.lang.annotation.*;

/**
 * Annotation to mark strategy classes
 * Links Topic + EventType to strategy implementation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandelEventStrategy {
    /**
     * The topic this strategy handles
     */
    Topic topic();
    
    /**
     * The specific event type within the topic
     */
    EventType eventType();
    
    /**
     * Priority for execution order (higher = executed first)
     */
    int priority() default 0;
}