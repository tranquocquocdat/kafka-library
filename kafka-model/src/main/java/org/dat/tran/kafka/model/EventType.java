package org.dat.tran.kafka.model;

public enum EventType {
    USER_CREATED("user.created"),
    USER_DELETED("user.deleted"),
    ORDER_CREATED("order.created"),
    ORDER_CANCELLED("order.cancelled"),
    PAYMENT_SUCCESS("payment.success"),
    PAYMENT_FAILED("payment.failed");

    private final String code;

    EventType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
