package com.notificationservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderCancelledEvent;
import com.notificationservice.service.NotificationService;

@Component
public class OrderCancelledEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderCancelledEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public OrderCancelledEventConsumer(
            ObjectMapper objectMapper,
            NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "order-cancelled",
            groupId = "notification-service"
    )
    public void consumeOrderCancelled(String message) {

        try {

            OrderCancelledEvent event =
                    objectMapper.readValue(
                            message,
                            OrderCancelledEvent.class
                    );

            log.info(
                    "Received OrderCancelledEvent: {}",
                    event
            );

            notificationService.sendOrderCancelledNotification(event);

        } catch (JsonProcessingException e) {

            log.error(
                    "Failed to deserialize OrderCancelledEvent: {}",
                    message,
                    e
            );
        }
    }
}