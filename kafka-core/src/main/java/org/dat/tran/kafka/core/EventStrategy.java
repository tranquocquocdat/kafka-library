// kafka-core/src/main/java/org/dat/tran/kafka/core/EventStrategy.java
package org.dat.tran.kafka.core;

/**
 * Main interface for event processing strategies
 * Each strategy handles one specific event type
 */
public interface EventStrategy<T> {
    /**
     * Process the event
     * @param event the event to process
     */
    void process(T event);
}