package com.notificationservice.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notificationservice.entity.ProcessedEvent;
import com.notificationservice.repository.ProcessedEventRepository;

@Service
public class EventIdempotencyService {

    private final ProcessedEventRepository processedEventRepository;

    public EventIdempotencyService(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public boolean alreadyProcessed(UUID eventId) {
        return processedEventRepository.existsById(eventId);
    }

    @Transactional
    public boolean markProcessed(UUID eventId, String eventType) {
        if (processedEventRepository.existsById(eventId)) {
            return false;
        }

        try {
            processedEventRepository.save(
                new ProcessedEvent(eventId, eventType)
            );
            return true;
        } catch (DataIntegrityViolationException e) {
            // Another consumer instance inserted the same event first.
            return false;
        }
    }
}