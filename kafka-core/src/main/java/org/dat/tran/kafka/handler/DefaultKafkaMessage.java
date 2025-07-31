// kafka-core/src/main/java/com/example/kafka/core/DefaultKafkaMessage.java
package org.dat.tran.kafka.handler;

/**
 * A simple implementation of KafkaMessage wrapping a payload.
 */
public class DefaultKafkaMessage<T> implements KafkaMessage<T> {
    private final String topic;
    private final T payload;

    public DefaultKafkaMessage(String topic, T payload) {
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public String getTopic() {
        return topic;
    }
    @Override
    public T getPayload() {
        return payload;
    }
}
