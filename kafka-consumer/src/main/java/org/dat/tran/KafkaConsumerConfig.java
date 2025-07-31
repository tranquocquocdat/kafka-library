// kafka-consumer/src/main/java/com/example/kafka/consumer/KafkaConsumerConfig.java
package org.dat.tran;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Kafka consumer configuration. Enable @KafkaListener support.
 */
@Configuration
@EnableKafka  // Enable Kafka listener infrastructure
public class KafkaConsumerConfig {
    // Spring Boot auto-configures ConsumerFactory via KafkaProperties
}
