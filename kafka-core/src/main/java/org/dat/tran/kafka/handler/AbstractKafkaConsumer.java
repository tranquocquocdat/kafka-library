// kafka-core/src/main/java/com/example/kafka/core/AbstractKafkaConsumer.java
package org.dat.tran.kafka.handler;

/**
 * Abstract base for Kafka consumers using the Template Method pattern.
 * The final handle() method defines the processing skeleton and delegates
 * to subclasses' process() method:contentReference[oaicite:4]{index=4}.
 */
public abstract class AbstractKafkaConsumer implements KafkaMessageHandler {
    @Override
    public final void handle(KafkaMessage<?> message) {
        // Pre-processing or validation could go here
        process(message);  // Delegate to subclass
        // Post-processing (e.g. acknowledgments) could go here
    }

    // Abstract step: subclasses implement business logic here
    protected abstract void process(KafkaMessage<?> message);
}
