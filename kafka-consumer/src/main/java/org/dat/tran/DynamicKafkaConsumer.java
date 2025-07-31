// kafka-consumer/src/main/java/com/example/kafka/consumer/DynamicKafkaConsumer.java
package org.dat.tran;


import jakarta.annotation.PostConstruct;
import org.dat.tran.kafka.handler.DefaultKafkaMessage;
import org.dat.tran.kafka.handler.KafkaHandlerRegistry;
import org.dat.tran.kafka.handler.KafkaMessage;
import org.dat.tran.kafka.handler.KafkaMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;

/**
 * Dynamically creates a Kafka listener container for each topic found in the registry.
 * Each incoming record is turned into a KafkaMessage and given to the handlers.
 */
@Component
public class DynamicKafkaConsumer {

    @Autowired
    private KafkaHandlerRegistry registry;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private ConsumerFactory<String, String> consumerFactory;

    @PostConstruct
    public void init() {
        // Build a ConsumerFactory using Spring Boot Kafka properties
        consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
        // For each topic, start a listener container
        for (String topic : registry.getTopics()) {
            ContainerProperties props = getContainerProperties(topic);
            ConcurrentMessageListenerContainer<String, String> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, props);
            container.start();
        }
    }

    private ContainerProperties getContainerProperties(String topic) {
        ContainerProperties props = new ContainerProperties(topic);
        props.setGroupId(groupId);
        // Define the message listener
        props.setMessageListener((org.springframework.kafka.listener.MessageListener<String, String>) record -> {
            // Wrap record in our KafkaMessage
            KafkaMessage<String> message = new DefaultKafkaMessage<>(record.topic(), record.value());
            // Dispatch to all handlers for this topic
            for (KafkaMessageHandler handler : registry.getHandlers(record.topic())) {
                handler.handle(message);
            }
        });
        return props;
    }
}
