package com.notificationservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notificationservice.entity.ProcessedEvent;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, UUID> {
}