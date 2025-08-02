package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.core.EventStrategy;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.registry.anotation.HandelEventStrategy;
import org.springframework.stereotype.Component;

/**
 * Secondary strategy for ORDER_CREATED events - handles analytics
 * This shows how you can have MULTIPLE strategies for the same event!
 */
@Component
@HandelEventStrategy(
    topic = Topic.ORDER_EVENT,
    eventType = EventType.ORDER_CREATED,
    priority = 5  // Lower priority - runs after main processing
)
public class OrderCreatedAnalyticsStrategy implements EventStrategy<OrderEvent> {
    
    @Override
    public void process(OrderEvent event) {
        System.out.println("📊 OrderCreatedAnalyticsStrategy processing: " + event);
        
        // Analytics logic:
        // 1. Track order metrics
        trackOrderMetrics(event);
        
        // 2. Update dashboards
        updateDashboards(event);
        
        // 3. Trigger recommendations
        triggerRecommendations(event);
        
        System.out.println("📈 Analytics completed for order: " + event.getOrderId());
    }
    
    private void trackOrderMetrics(OrderEvent event) {
        System.out.println("📊 Tracking metrics for order: " + event.getOrderId());
        // Analytics service call here
    }
    
    private void updateDashboards(OrderEvent event) {
        System.out.println("📈 Updating dashboards with order: " + event.getOrderId());
        // Dashboard service call here
    }
    
    private void triggerRecommendations(OrderEvent event) {
        System.out.println("🎯 Triggering recommendations for order: " + event.getOrderId());
        // Recommendation engine call here
    }
}
