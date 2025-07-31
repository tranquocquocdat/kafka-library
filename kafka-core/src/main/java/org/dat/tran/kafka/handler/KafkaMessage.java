package org.dat.tran.kafka.handler;// kafka-core/src/main/java/com/example/kafka/core/org.dat.tran.KafkaMessage.java


public interface KafkaMessage<T> {
    String getTopic();
    T getPayload();
}
