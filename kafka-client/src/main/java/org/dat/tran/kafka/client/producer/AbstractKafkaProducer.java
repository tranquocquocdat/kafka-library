package org.dat.tran.kafka.client.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Base class for all Kafka producers
 * Provides common functionality like validation and sending
 */
public abstract class AbstractKafkaProducer<T> {

    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    private final String topic;

    protected AbstractKafkaProducer(String topic) {
        this.topic = topic;
    }

    public void send(T event) {
        // Simple validation
        if (event == null) {
            System.err.println("❌ Cannot send null event");
            return;
        }
        
        System.out.println("📤 Producing to topic " + topic + ": " + event);
        kafkaTemplate.send(topic, event);
    }
    
    public String getTopic() {
        return topic;
    }
}
