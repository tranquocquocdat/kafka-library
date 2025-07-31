// kafka-core/src/main/java/com/example/kafka/core/KafkaMessageHandler.java
package org.dat.tran.kafka.handler;

public interface KafkaMessageHandler {
    void handle(KafkaMessage<?> message);
}
