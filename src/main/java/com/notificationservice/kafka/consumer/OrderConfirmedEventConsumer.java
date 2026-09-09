package com.notificationservice.kafka.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderConfirmedEvent;
import com.notificationservice.service.NotificationService;

@Component
public class OrderConfirmedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderConfirmedEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public OrderConfirmedEventConsumer(
            ObjectMapper objectMapper,
            NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "order-confirmed",
            groupId = "notification-service"
    )
    public void consumeOrderConfirmed(String message) {

        try {

            OrderConfirmedEvent event =
                    objectMapper.readValue(
                            message,
                            OrderConfirmedEvent.class
                    );

            log.info(
                    "Received OrderCancelledEvent: {}",
                    event
            );

            notificationService.sendOrderConfirmedNotification(event);

        } catch (JsonProcessingException e) {

            log.error(
                    "Failed to deserialize OrderConfirmedEvent: {}",
                    message,
                    e
            );
        }
    }


}
