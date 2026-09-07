package com.notificationservice.kafka.event;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderCreatedEvent {
	
	private UUID orderId;
	
	private UUID userId;
	
	private BigDecimal totalAmount;

	private String email;
	
	public OrderCreatedEvent() {
	
	}

	public OrderCreatedEvent(UUID orderId, UUID userId, BigDecimal totalAmount,String email) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.totalAmount = totalAmount;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}

