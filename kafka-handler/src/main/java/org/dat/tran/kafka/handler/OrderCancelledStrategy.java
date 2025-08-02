package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.core.EventStrategy;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.registry.anotation.HandelEventStrategy;
import org.springframework.stereotype.Component;

/**
 * Strategy for handling ORDER_CANCELLED events
 */
@Component
@HandelEventStrategy(
    topic = Topic.ORDER_EVENT,
    eventType = EventType.ORDER_CANCELLED,
    priority = 10
)
public class OrderCancelledStrategy implements EventStrategy<OrderEvent> {
    
    @Override
    public void process(OrderEvent event) {
        System.out.println("❌ OrderCancelledStrategy processing: " + event);
        
        // Your business logic here:
        // 1. Release inventory
        releaseInventory(event.getOrderId());
        
        // 2. Refund payment
        refundPayment(event.getAmount());
        
        // 3. Send cancellation email
        sendCancellationEmail(event.getOrderId());
        
        // 4. Update analytics
        logOrderCancellation(event);
        
        System.out.println("✅ Order cancellation completed: " + event.getOrderId());
    }
    
    private void releaseInventory(String orderId) {
        System.out.println("📦 Releasing inventory for order: " + orderId);
        // Inventory service call here
    }
    
    private void refundPayment(double amount) {
        System.out.println("💰 Processing refund: $" + amount);
        // Payment service call here
    }
    
    private void sendCancellationEmail(String orderId) {
        System.out.println("📧 Sending cancellation email for order: " + orderId);
        // Email service call here
    }
    
    private void logOrderCancellation(OrderEvent event) {
        System.out.println("📊 Logging order cancellation: " + event.getOrderId());
        // Analytics service call here
    }
}