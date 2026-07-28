package com.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.notificationservice.kafka.event.UserRegisteredEvent;

@Service
public class NotificationService {
	
	private static final Logger log=LoggerFactory.getLogger(NotificationService.class);
	
	private final EmailService emailService;
	
	

	public NotificationService(EmailService emailService) {
		this.emailService = emailService;
	}



	public void sendWelcomeNotification(UserRegisteredEvent event) {
		
		
		 	log.info("==============================================");
	        log.info("Preparing Welcome Notification");
	        log.info("User : {}", event.getName());
	        log.info("Email: {}", event.getEmail());
	        log.info("Registered At: {}", event.getRegisteredAt());
	        log.info("Sending welcome email to {}", event.getEmail());
	        log.info("==============================================");
	        
	        emailService.sendWelcomeEmail(
	                event.getEmail(),
	                event.getName()
	        );
		
		
		
		
	}

}
