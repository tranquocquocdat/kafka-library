package org.dat.tran.kafka.app;

import org.dat.tran.kafka.client.producer.OrderEventProducer;
import org.dat.tran.kafka.client.producer.UserEventProducer;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.UserEvent;
import org.dat.tran.kafka.registry.EventStrategyRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Test runner to demonstrate the system working
 * Runs after application startup and sends test messages
 */
@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    private UserEventProducer userEventProducer;

    @Autowired
    private OrderEventProducer orderEventProducer;
    
    @Autowired
    private EventStrategyRegistry registry;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n🧪 Starting End-to-End Test...");
        
        // Wait for consumers to be ready
        Thread.sleep(2000);
        
        // Show what strategies are registered
        showRegistryInfo();
        
        Thread.sleep(1000);
        
        // Test user events
        testUserEvents();
        
        Thread.sleep(2000);
        
        // Test order events
        testOrderEvents();
        
        System.out.println("\n✅ End-to-End test completed!");
    }
    
    private void showRegistryInfo() {
        System.out.println("📊 Registry Information:");
        System.out.println("Active topics: " + registry.getActiveTopics());
        
        // Show which combinations have strategies
        System.out.println("Strategy combinations:");
        if (registry.hasStrategies(org.dat.tran.kafka.model.Topic.USER_EVENT, EventType.USER_CREATED)) {
            System.out.println("  ✅ USER_EVENT.USER_CREATED");
        }
        if (registry.hasStrategies(org.dat.tran.kafka.model.Topic.USER_EVENT, EventType.USER_DELETED)) {
            System.out.println("  ✅ USER_EVENT.USER_DELETED");
        }
        if (registry.hasStrategies(org.dat.tran.kafka.model.Topic.ORDER_EVENT, EventType.ORDER_CREATED)) {
            System.out.println("  ✅ ORDER_EVENT.ORDER_CREATED");
        }
        if (registry.hasStrategies(org.dat.tran.kafka.model.Topic.ORDER_EVENT, EventType.ORDER_CANCELLED)) {
            System.out.println("  ✅ ORDER_EVENT.ORDER_CANCELLED");
        }
    }
    
    private void testUserEvents() {
        System.out.println("\n👤 Testing User Events...");
        
        // Test USER_CREATED
        UserEvent userCreatedEvent = new UserEvent("u123", "alice", EventType.USER_CREATED);
        userEventProducer.send(userCreatedEvent);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Test USER_DELETED
        UserEvent userDeletedEvent = new UserEvent("u456", "bob", EventType.USER_DELETED);
        userEventProducer.send(userDeletedEvent);
    }
    
    private void testOrderEvents() {
        System.out.println("\n🛒 Testing Order Events...");
        
        // Test ORDER_CREATED
        OrderEvent orderCreatedEvent = new OrderEvent("o789", 99.99, EventType.ORDER_CREATED);
        orderEventProducer.send(orderCreatedEvent);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Test ORDER_CANCELLED
        OrderEvent orderCancelledEvent = new OrderEvent("o101", 149.99, EventType.ORDER_CANCELLED);
        orderEventProducer.send(orderCancelledEvent);
    }
}
