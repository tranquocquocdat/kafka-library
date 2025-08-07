// kafka-model/src/main/java/com/example/kafka/model/UserEvent.java
package org.dat.tran.kafka.model;

public class UserEvent{
    private String userId;
    private String username;
    private EventType eventType;

    public UserEvent() {}
    public UserEvent(String userId, String username, EventType eventType) {
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
    }
    // getters and setters

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    @Override
    public String toString() {
        return "UserEvent{userId='" + userId + "', username='" + username + "', eventType=" + eventType + '}';
    }
}
