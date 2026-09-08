package com.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.notificationservice.kafka.event.OrderCancelledEvent;
import com.notificationservice.kafka.event.OrderCreatedEvent;
import com.notificationservice.kafka.event.UserRegisteredEvent;

@Service
public class NotificationService {
	
	private static final Logger log=LoggerFactory.getLogger(NotificationService.class);
	
	private final EmailService emailService;
	
	

	public NotificationService(EmailService emailService) {
		this.emailService = emailService;
	}



	public void sendWelcomeNotification(UserRegisteredEvent event) {
		
		
	        emailService.sendWelcomeEmail(
	                event.getEmail(),
	                event.getName()
	        );
		
	}
	
	public void sendOrderCreatedNotification(OrderCreatedEvent event) {
		
		log.info("processing order notification for order: {}",event.getOrderId() );

		emailService.sendOrderCreatedEmail(event.getEmail(), event.getOrderId(), event.getTotalAmount());
		
		
	}



	public void sendOrderCancelledNotification(OrderCancelledEvent event) {

	    log.info(
	            "Sending cancellation notification for order: {}",
	            event.getOrderId()
	    );

	    emailService.sendOrderCancelledEmail(
	            event.getEmail(),
	            event.getOrderId()
	    );
	}

}
