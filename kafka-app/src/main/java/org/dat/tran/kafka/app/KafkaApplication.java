package org.dat.tran.kafka.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class
 * Scans all kafka packages to find components
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.dat.tran.kafka")
public class KafkaApplication {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Simple Kafka Application...");
        SpringApplication.run(KafkaApplication.class, args);
        System.out.println("✅ Kafka Application started successfully!");
    }
}