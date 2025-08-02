package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.core.EventStrategy;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.registry.annotation.HandelEventStrategy;
import org.springframework.stereotype.Component;

/**
 * Primary strategy for handling ORDER_CREATED events
 */
@Component
@HandelEventStrategy(
    topic = Topic.ORDER_EVENT,
    eventType = EventType.ORDER_CREATED,
    priority = 10  // High priority - main processing
)
public class OrderCreatedStrategy implements EventStrategy<OrderEvent> {
    
    @Override
    public void process(OrderEvent event) {
        System.out.println("🛒 OrderCreatedStrategy processing: " + event);
        
        // Your business logic here:
        // 1. Validate order
        boolean isValid = validateOrder(event);
        if (!isValid) {
            System.err.println("❌ Order validation failed: " + event.getOrderId());
            return;
        }
        
        // 2. Reserve inventory
        reserveInventory(event.getOrderId());
        
        // 3. Process payment
        processPayment(event.getAmount());
        
        // 4. Send confirmation
        sendOrderConfirmation(event.getOrderId());
        
        System.out.println("✅ Order created successfully: " + event.getOrderId());
    }
    
    private boolean validateOrder(OrderEvent event) {
        System.out.println("✔️ Validating order: " + event.getOrderId());
        return event.getAmount() > 0; // Simple validation
    }
    
    private void reserveInventory(String orderId) {
        System.out.println("📦 Reserving inventory for order: " + orderId);
        // Inventory service call here
    }
    
    private void processPayment(double amount) {
        System.out.println("💳 Processing payment: $" + amount);
        // Payment service call here
    }
    
    private void sendOrderConfirmation(String orderId) {
        System.out.println("📧 Sending confirmation for order: " + orderId);
        // Email service call here
    }
}