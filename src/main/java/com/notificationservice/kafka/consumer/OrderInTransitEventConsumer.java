package com.notificationservice.kafka.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderConfirmedEvent;
import com.notificationservice.kafka.event.OrderInTransitEvent;
import com.notificationservice.service.NotificationService;

@Component
public class OrderInTransitEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderConfirmedEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public OrderInTransitEventConsumer(
            ObjectMapper objectMapper,
            NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "order-intransit",
            groupId = "notification-service"
    )
    public void consumeOrderInTransit(String message) {

        try {

        	OrderInTransitEvent event =
                    objectMapper.readValue(
                            message,
                            OrderInTransitEvent.class
                    );

            log.info(
                    "Received OrderInTransitEvent: {}",
                    event
            );

            notificationService.sendOrderInTransitNotification(event);

        } catch (JsonProcessingException e) {

            log.error(
                    "Failed to deserialize OrderInTransitEvent: {}",
                    message,
                    e
            );
        }
    }


}
