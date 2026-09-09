package com.notificationservice.kafka.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderCompletedEvent;
import com.notificationservice.kafka.event.OrderConfirmedEvent;
import com.notificationservice.service.NotificationService;

@Component
public class OrderCompletedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderConfirmedEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public OrderCompletedEventConsumer(
            ObjectMapper objectMapper,
            NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "order-completed",
            groupId = "notification-service"
    )
    public void consumeOrderCompleted(String message) {

        try {

            OrderCompletedEvent event =
                    objectMapper.readValue(
                            message,
                            OrderCompletedEvent.class
                    );

            log.info(
                    "Received OrderCompletedEvent: {}",
                    event
            );

            notificationService.sendOrderCompletedNotification(event);

        } catch (JsonProcessingException e) {

            log.error(
                    "Failed to deserialize OrderCompletedEvent: {}",
                    message,
                    e
            );
        }
    }


}
