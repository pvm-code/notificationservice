package com.notificationservice.kafka.event;
import java.util.UUID;

public class OrderInTransitEvent {

    private UUID orderId;

    private UUID userId;

    private String email;

    public OrderInTransitEvent() {
    }

    public OrderInTransitEvent(
            UUID orderId,
            UUID userId,
            String email) {

        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}