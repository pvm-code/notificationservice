package com.notificationservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderCancelledEvent;
import com.notificationservice.service.EventIdempotencyService;
import com.notificationservice.service.NotificationService;

@Component
public class OrderCancelledEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderCancelledEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final EventIdempotencyService eventIdempotencyService;

    public OrderCancelledEventConsumer(
            ObjectMapper objectMapper,
            NotificationService notificationService,
            EventIdempotencyService eventIdempotencyService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.eventIdempotencyService = eventIdempotencyService;
    }

    @KafkaListener(
            topics = "order-cancelled",
            groupId = "notification-service"
    )
    public void consume(String message) {

        try {
            OrderCancelledEvent event =
                    objectMapper.readValue(message, OrderCancelledEvent.class);

            if (event.getEventId() == null) {
                log.error(
                        "Received order-cancelled event without eventId. orderId={}",
                        event.getOrderId()
                );
                throw new IllegalStateException("eventId is required");
            }

            if (eventIdempotencyService.alreadyProcessed(event.getEventId())) {
                log.info(
                        "Duplicate event ignored. eventId={}, orderId={}",
                        event.getEventId(),
                        event.getOrderId()
                );
                return;
            }

            notificationService.sendOrderCancelledNotification(event);

            boolean marked = eventIdempotencyService.markProcessed(
                    event.getEventId(),
                    "ORDER_CANCELLED"
            );

            if (!marked) {
                log.info(
                        "Event was already processed concurrently. eventId={}, orderId={}",
                        event.getEventId(),
                        event.getOrderId()
                );
            }

        } catch (Exception e) {
            log.error("Failed to process order-cancelled event", e);
            throw new RuntimeException(
                    "Failed to process order-cancelled event", e);
        }
    }
}