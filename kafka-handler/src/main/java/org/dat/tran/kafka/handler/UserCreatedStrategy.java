package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.core.EventStrategy;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.model.UserEvent;
import org.dat.tran.kafka.registry.anotation.HandelEventStrategy;
import org.springframework.stereotype.Component;

/**
 * Strategy for handling USER_CREATED events
 * This is a concrete implementation of business logic
 */
@Component
@HandelEventStrategy(
    topic = Topic.USER_EVENT,
    eventType = EventType.USER_CREATED,
    priority = 10
)
public class UserCreatedStrategy implements EventStrategy<UserEvent> {
    
    @Override
    public void process(UserEvent event) {
        System.out.println("🎉 UserCreatedStrategy processing: " + event);
        
        // Your business logic here:
        // 1. Send welcome email
        sendWelcomeEmail(event.getUsername());
        
        // 2. Create user profile
        createUserProfile(event.getUserId());
        
        // 3. Log analytics
        logUserCreation(event);
        
        System.out.println("✅ User creation completed for: " + event.getUsername());
    }
    
    private void sendWelcomeEmail(String username) {
        System.out.println("📧 Sending welcome email to: " + username);
        // Email service call here
    }
    
    private void createUserProfile(String userId) {
        System.out.println("👤 Creating user profile for: " + userId);
        // Database call here
    }
    
    private void logUserCreation(UserEvent event) {
        System.out.println("📊 Logging user creation: " + event.getUserId());
        // Analytics service call here
    }
}