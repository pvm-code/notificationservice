package com.notificationservice.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.notificationservice.metrics.MetricsService;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
	private final MetricsService metricsService;

	private static final Logger log=LoggerFactory.getLogger(NotificationService.class);

	

    public EmailService(JavaMailSender mailSender, MetricsService metricsService) {
		super();
		this.mailSender = mailSender;
		this.metricsService = metricsService;
	}



	public void sendWelcomeEmail(String toEmail, String userName) {
		
		try {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(toEmail);
	        message.setSubject("Welcome to Our Platform 🎉");

	        message.setText(
	                "Hi " + userName + ",\n\n"
	                + "Welcome to our platform!\n\n"
	                + "Your account has been created successfully.\n"
	                + "We're excited to have you with us.\n\n"
	                + "Happy Learning!\n\n"
	                + "Regards,\n"
	                + "Notification Service"
	        );

	        mailSender.send(message);
	        metricsService.incrementEmailSent();
	        
		} catch (Exception e) {
		    metricsService.incrementEmailFailed();
		}
        
    }
	public void sendOrderCreatedEmail(
	        String toEmail,
	        UUID orderId,
	        BigDecimal totalAmount) {

	    try {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(toEmail);
	        message.setSubject("Order Created Successfully");

	        message.setText(
	                "Hello,\n\n"
	                + "Your order has been created successfully.\n\n"
	                + "Order ID: " + orderId + "\n"
	                + "Total Amount: ₹" + totalAmount + "\n\n"
	                + "Thank you for your order!\n\n"
	                + "Regards,\n"
	                + "Notification Service"
	        );

	        mailSender.send(message);
	        metricsService.incrementEmailSent();

	    } catch (Exception e) {
	        metricsService.incrementEmailFailed();
	        log.error("Failed to send order confirmation email", e);
	    }
	}



	public void sendOrderCancelledEmail(
	        String toEmail,
	        UUID orderId) {

	    try {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(toEmail);
	        message.setSubject("Order Cancelled");

	        message.setText(
	                "Hello,\n\n"
	                + "Your order has been cancelled successfully.\n\n"
	                + "Order ID: " + orderId + "\n\n"
	                + "If you did not request this cancellation, "
	                + "please contact our support team.\n\n"
	                + "Regards,\n"
	                + "Notification Service"
	        );

	        mailSender.send(message);

	        metricsService.incrementEmailSent();

	    } catch (Exception e) {

	        metricsService.incrementEmailFailed();

	        log.error(
	                "Failed to send order cancellation email",
	                e
	        );
	    }
	}



	public void sendOrderConfirmedEmail(String toEmail, UUID orderId) {
		 try {

		        SimpleMailMessage message = new SimpleMailMessage();

		        message.setTo(toEmail);
		        message.setSubject("Order Confirmed");

		        message.setText(
		                "Hello,\n\n"
		                + "Your order has been confirmed successfully.\n\n"
		                + "Order ID: " + orderId + "\n\n"
		                + "order will be dispatched soon , "
		                + "please contact our support team for any help.\n\n"
		                + "Regards,\n"
		                + "Notification Service"
		        );

		        mailSender.send(message);

		        metricsService.incrementEmailSent();

		    } catch (Exception e) {

		        metricsService.incrementEmailFailed();

		        log.error(
		                "Failed to send order cancellation email",
		                e
		        );
		    }		
	}



	public void sendOrderInTransitEmail(String toEmail, UUID orderId) {
		try {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(toEmail);
	        message.setSubject("Order in transit");

	        message.setText(
	                "Hello,\n\n"
	                + "Your order is on the way.\n\n"
	                + "Order ID: " + orderId + "\n\n"
	                + "order  dispatched  , "
	                + "please contact our support team for any help.\n\n"
	                + "Regards,\n"
	                + "Notification Service"
	        );

	        mailSender.send(message);

	        metricsService.incrementEmailSent();

	    } catch (Exception e) {

	        metricsService.incrementEmailFailed();

	        log.error(
	                "Failed to send order cancellation email",
	                e
	        );
	    }				
	}



	public void sendOrderCompletedEmail(String toEmail, UUID orderId) {
		try {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(toEmail);
	        message.setSubject("Order completed");

	        message.setText(
	                "Hello,\n\n"
	                + "Your order is delivered successfully.\n\n"
	                + "Order ID: " + orderId + "\n\n"
	                + "give feedback on ****  , "
	                + "please contact our support team for any help.\n\n"
	                + "Regards,\n"
	                + "Notification Service"
	        );

	        mailSender.send(message);

	        metricsService.incrementEmailSent();

	    } catch (Exception e) {

	        metricsService.incrementEmailFailed();

	        log.error(
	                "Failed to send order cancellation email",
	                e
	        );
	    }		
	}
}