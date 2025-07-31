// kafka-app/src/main/java/com/example/kafka/app/KafkaApplication.java
package org.dat.tran.kafka.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class.
 * @EnableKafka is on consumer config if needed.
 */
@SpringBootApplication
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}
