package com.notificationservice.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.UserRegisteredEvent;
import com.notificationservice.service.NotificationService;

@Component
public class UserEventConsumer {
	
	private final NotificationService notificationService;
	
	private final ObjectMapper objectMapper;

	
	
	public UserEventConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
		this.notificationService = notificationService;
		this.objectMapper = objectMapper;
	}


	@KafkaListener(
			
			topics = "user-registered"
			
			)
	public void consume(String message) {
		
		try {
			UserRegisteredEvent event= objectMapper.readValue(message, UserRegisteredEvent.class);
			
			notificationService.sendWelcomeNotification(event);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
