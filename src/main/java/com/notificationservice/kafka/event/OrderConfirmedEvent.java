package com.notificationservice.kafka.event;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderConfirmedEvent {

	private UUID orderId;
	private UUID userId;
	private String email;
	private BigDecimal totalAmount;
	
	private UUID eventId;

	public UUID getEventId() {
	    return eventId;
	}

	public void setEventId(UUID eventId) {
	    this.eventId = eventId;
	}
	
	
	public OrderConfirmedEvent(
	        UUID orderId,
	        UUID userId,
	        String email,
	        BigDecimal totalAmount) {

	    this.orderId = orderId;
	    this.userId = userId;
	    this.email = email;
	    this.totalAmount = totalAmount;
	}
	
	public OrderConfirmedEvent() {
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

}
