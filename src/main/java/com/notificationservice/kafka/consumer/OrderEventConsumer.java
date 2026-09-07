package com.notificationservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderCreatedEvent;
import com.notificationservice.service.NotificationService;

@Component
public class OrderEventConsumer {
	
	private static final Logger log = 
			LoggerFactory.getLogger(OrderEventConsumer.class);
	
	
	private final ObjectMapper objectMapper;
	
	private final NotificationService notificationService;


	public OrderEventConsumer(ObjectMapper objectMapper,NotificationService notificationService) {
		super();
		this.objectMapper = objectMapper;
		this.notificationService = notificationService;
	}
	
	@KafkaListener(
			
			topics = "order-created",
			groupId = "notification-service"
			
			)
	public void consumerOrderCreate(String message) {
		
		
		try {
			OrderCreatedEvent  event = 
					objectMapper.readValue(message, OrderCreatedEvent.class);
			
					log.info("Received ordercreatedEvent: {}",event);
					notificationService.sendOrderCreatedNotification(event);
			
			
		} catch (JsonProcessingException e) {
			log.error("failed to deserialixe orderCreatedEvent: {}",message,e);
		}
		
		
		
	}

}
