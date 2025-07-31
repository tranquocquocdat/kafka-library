// kafka-producer/src/main/java/com/example/kafka/producer/MessageValidator.java
package org.dat.tran.kafka.producer;

/**
 * Validator interface for outgoing messages.
 */
public interface MessageValidator<T> {
    boolean isValid(T message);
}
