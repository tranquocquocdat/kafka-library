// kafka-strategy/src/main/java/com/example/kafka/strategy/EventStrategy.java
package org.dat.tran.kafka.handler;

public interface EventStrategy<T> {
    void process(T event);
}
