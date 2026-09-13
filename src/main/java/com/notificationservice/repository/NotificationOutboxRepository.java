package com.notificationservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notificationservice.entity.NotificationOutbox;
import com.notificationservice.entity.NotificationOutboxStatus;

public interface NotificationOutboxRepository
        extends JpaRepository<NotificationOutbox, UUID> {

    List<NotificationOutbox> findTop50ByStatusOrderByCreatedAtAsc(
            NotificationOutboxStatus status);

    @Modifying
    @Query("""
        UPDATE NotificationOutbox n
        SET n.status = :processing,
            n.processingStartedAt = :processingStartedAt
        WHERE n.id = :id
          AND n.status = :pending
    """)
    int claimNotification(
            @Param("id") UUID id,
            @Param("pending") NotificationOutboxStatus pending,
            @Param("processing") NotificationOutboxStatus processing,
            @Param("processingStartedAt") LocalDateTime processingStartedAt
    );

    @Modifying
    @Query("""
        UPDATE NotificationOutbox n
        SET n.status = :pending,
            n.processingStartedAt = NULL
        WHERE n.status = :processing
          AND n.processingStartedAt < :cutoff
    """)
    int resetStaleProcessingNotifications(
            @Param("processing") NotificationOutboxStatus processing,
            @Param("pending") NotificationOutboxStatus pending,
            @Param("cutoff") LocalDateTime cutoff
    );
}