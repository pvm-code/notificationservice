package com.notificationservice.outbox;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.notificationservice.entity.NotificationOutbox;
import com.notificationservice.entity.NotificationOutboxStatus;
import com.notificationservice.repository.NotificationOutboxRepository;
import com.notificationservice.service.EmailService;

@Component
public class NotificationOutboxPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationOutboxPublisher.class);

    private static final int STALE_PROCESSING_MINUTES = 5;

    private final NotificationOutboxRepository repository;
    private final EmailService emailService;

    public NotificationOutboxPublisher(
            NotificationOutboxRepository repository,
            EmailService emailService) {

        this.repository = repository;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelay = 10000)
    public void publishPendingNotifications() {

        resetStaleProcessingNotifications();

        List<NotificationOutbox> notifications =
                repository.findTop50ByStatusOrderByCreatedAtAsc(
                        NotificationOutboxStatus.PENDING);

        for (NotificationOutbox notification : notifications) {

            int claimed = claimNotification(notification);

            if (claimed == 0) {
                continue;
            }

            sendNotification(notification);
        }
    }

    @Transactional
    protected int claimNotification(
            NotificationOutbox notification) {

        return repository.claimNotification(
                notification.getId(),
                NotificationOutboxStatus.PENDING,
                NotificationOutboxStatus.PROCESSING,
                LocalDateTime.now());
    }

    private void sendNotification(
            NotificationOutbox notification) {

        try {

            emailService.sendEmail(
                    notification.getRecipient(),
                    notification.getSubject(),
                    notification.getBody());

            markAsSent(notification);

            log.info(
                    "Notification sent successfully. notificationId={}, eventId={}, eventType={}",
                    notification.getId(),
                    notification.getEventId(),
                    notification.getEventType());

        } catch (Exception e) {

            resetToPending(notification);

            log.error(
                    "Failed to send notification. notificationId={}, eventId={}, eventType={}",
                    notification.getId(),
                    notification.getEventId(),
                    notification.getEventType(),
                    e);
        }
    }

    @Transactional
    protected void markAsSent(
            NotificationOutbox notification) {

        notification.setStatus(NotificationOutboxStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        notification.setProcessingStartedAt(null);

        repository.save(notification);
    }

    @Transactional
    protected void resetToPending(
            NotificationOutbox notification) {

        notification.setStatus(NotificationOutboxStatus.PENDING);
        notification.setProcessingStartedAt(null);

        repository.save(notification);
    }

    @Transactional
    protected void resetStaleProcessingNotifications() {

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusMinutes(STALE_PROCESSING_MINUTES);

        int resetCount =
                repository.resetStaleProcessingNotifications(
                        NotificationOutboxStatus.PROCESSING,
                        NotificationOutboxStatus.PENDING,
                        cutoff);

        if (resetCount > 0) {

            log.warn(
                    "Reset {} stale notification(s) to PENDING",
                    resetCount);
        }
    }
}