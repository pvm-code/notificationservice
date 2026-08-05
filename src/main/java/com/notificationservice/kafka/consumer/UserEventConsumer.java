package com.notificationservice.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.UserRegisteredEvent;
import com.notificationservice.metrics.MetricsService;
import com.notificationservice.service.NotificationService;

@Component
public class UserEventConsumer {
	
	private final NotificationService notificationService;
	
	private final ObjectMapper objectMapper;
	
	private final MetricsService metricsService;


	
	
	public UserEventConsumer(NotificationService notificationService, ObjectMapper objectMapper,MetricsService metricsService) {
		this.notificationService = notificationService;
		this.objectMapper = objectMapper;
		this.metricsService = metricsService;

	}


	@KafkaListener(topics = "user-registered")
	public void consume(String message) {

	    System.out.println("Received Kafka message: " + message);

	    try {
	        UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);

	        System.out.println("JSON parsed successfully");

	        notificationService.sendWelcomeNotification(event);

	        System.out.println("Notification sent");

	        metricsService.incrementKafkaConsumed();

	        System.out.println("Kafka counter incremented");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
