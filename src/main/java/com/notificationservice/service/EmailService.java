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

    private static final Logger log =
            LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final MetricsService metricsService;

    public EmailService(
            JavaMailSender mailSender,
            MetricsService metricsService) {

        this.mailSender = mailSender;
        this.metricsService = metricsService;
    }

    public void sendWelcomeEmail(
            String toEmail,
            String userName) {

        sendEmail(
                toEmail,
                "Welcome to Our Platform 🎉",
                "Hi " + userName + ",\n\n"
                        + "Welcome to our platform!\n\n"
                        + "Your account has been created successfully.\n"
                        + "We're excited to have you with us.\n\n"
                        + "Happy Learning!\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    public void sendOrderCreatedEmail(
            String toEmail,
            UUID orderId,
            BigDecimal totalAmount) {

        sendEmail(
                toEmail,
                "Order Created Successfully",
                "Hello,\n\n"
                        + "Your order has been created successfully.\n\n"
                        + "Order ID: " + orderId + "\n"
                        + "Total Amount: ₹" + totalAmount + "\n\n"
                        + "Thank you for your order!\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    public void sendOrderCancelledEmail(
            String toEmail,
            UUID orderId) {

        sendEmail(
                toEmail,
                "Order Cancelled",
                "Hello,\n\n"
                        + "Your order has been cancelled successfully.\n\n"
                        + "Order ID: " + orderId + "\n\n"
                        + "If you did not request this cancellation, "
                        + "please contact our support team.\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    public void sendOrderConfirmedEmail(
            String toEmail,
            UUID orderId) {

        sendEmail(
                toEmail,
                "Order Confirmed",
                "Hello,\n\n"
                        + "Your order has been confirmed successfully.\n\n"
                        + "Order ID: " + orderId + "\n\n"
                        + "Your order will be dispatched soon.\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    public void sendOrderInTransitEmail(
            String toEmail,
            UUID orderId) {

        sendEmail(
                toEmail,
                "Order in Transit",
                "Hello,\n\n"
                        + "Your order is on the way.\n\n"
                        + "Order ID: " + orderId + "\n\n"
                        + "Your order has been dispatched.\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    public void sendOrderCompletedEmail(
            String toEmail,
            UUID orderId) {

        sendEmail(
                toEmail,
                "Order Completed",
                "Hello,\n\n"
                        + "Your order has been delivered successfully.\n\n"
                        + "Order ID: " + orderId + "\n\n"
                        + "Thank you for shopping with us.\n\n"
                        + "Regards,\n"
                        + "Notification Service"
        );
    }

    private void sendEmail(
            String toEmail,
            String subject,
            String text) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);

            metricsService.incrementEmailSent();

        } catch (Exception e) {

            metricsService.incrementEmailFailed();

            log.error(
                    "Failed to send email. recipient={}, subject={}",
                    toEmail,
                    subject,
                    e
            );

            throw new RuntimeException(
                    "Failed to send email",
                    e
            );
        }
    }
}