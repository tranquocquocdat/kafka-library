// kafka-producer/src/main/java/com/example/kafka/producer/AbstractKafkaProducer.java
package org.dat.tran.kafka.producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Base producer with a KafkaTemplate and a MessageValidator.
 */
public abstract class AbstractKafkaProducer<T> {

    @Autowired
    protected KafkaTemplate<String, T> kafkaTemplate;

    @Autowired
    protected MessageValidator<T> validator;

    private final String topic;

    protected AbstractKafkaProducer(String topic) {
        this.topic = topic;
    }

    public void send(T event) {
        if (validator.isValid(event)) {
            System.out.println("Producing to topic " + topic + ": " + event);
            kafkaTemplate.send(topic, event);
        } else {
            System.err.println("Invalid message: " + event);
        }
    }
}
