package com.notificationservice.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notificationservice.entity.NotificationOutbox;
import com.notificationservice.entity.NotificationOutboxStatus;
import com.notificationservice.repository.NotificationOutboxRepository;

@Service
public class NotificationOutboxService {

    private final NotificationOutboxRepository repository;

    public NotificationOutboxService(
            NotificationOutboxRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public boolean createNotification(
            UUID eventId,
            String eventType,
            String recipient,
            String subject,
            String body) {

        NotificationOutbox notification = new NotificationOutbox();

        notification.setEventId(eventId);
        notification.setEventType(eventType);
        notification.setRecipient(recipient);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setStatus(NotificationOutboxStatus.PENDING);

        try {
            repository.save(notification);
            return true;

        } catch (DataIntegrityViolationException e) {
            // eventId is unique.
            // Another consumer instance already created this notification.
            return false;
        }
    }
}