package com.notificationservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String toEmail, String userName) {

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
    }
}