// kafka-model/src/main/java/com/example/kafka/model/OrderEvent.java
package org.dat.tran.kafka.model;

import org.dat.tran.kafka.core.Event;

public class OrderEvent extends Event {
    private String orderId;
    private double amount;
    private EventType eventType;

    public OrderEvent() {}
    public OrderEvent(String orderId, double amount, EventType eventType) {
        this.orderId = orderId;
        this.amount = amount;
        this.eventType = eventType;
    }
    // getters and setters

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    @Override
    public String toString() {
        return "OrderEvent{orderId='" + orderId + "', amount=" + amount + ", eventType=" + eventType + '}';
    }
}
