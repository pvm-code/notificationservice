package com.notificationservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.kafka.event.OrderCreatedEvent;
import com.notificationservice.service.EventIdempotencyService;
import com.notificationservice.service.NotificationOutboxService;

@Component
public class OrderEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final NotificationOutboxService notificationOutboxService;
    private final EventIdempotencyService eventIdempotencyService;

    public OrderEventConsumer(
            ObjectMapper objectMapper,
            NotificationOutboxService notificationOutboxService,
            EventIdempotencyService eventIdempotencyService) {

        this.objectMapper = objectMapper;
        this.notificationOutboxService = notificationOutboxService;
        this.eventIdempotencyService = eventIdempotencyService;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-service"
    )
    public void consume(String message) {

        try {

            OrderCreatedEvent event =
                    objectMapper.readValue(
                            message,
                            OrderCreatedEvent.class);

            if (event.getEventId() == null) {

                log.error(
                        "Received order-created event without eventId. orderId={}",
                        event.getOrderId());

                throw new IllegalStateException(
                        "eventId is required for order-created event");
            }

            if (eventIdempotencyService.alreadyProcessed(
                    event.getEventId())) {

                log.info(
                        "Duplicate event ignored. eventId={}, orderId={}",
                        event.getEventId(),
                        event.getOrderId());

                return;
            }

            String subject = "Order Created Successfully";

            String body =
                    "Hello,\n\n"
                    + "Your order has been created successfully.\n\n"
                    + "Order ID: " + event.getOrderId() + "\n"
                    + "Total Amount: ₹" + event.getTotalAmount() + "\n\n"
                    + "Thank you for your order!\n\n"
                    + "Regards,\n"
                    + "Notification Service";

            boolean created =
                    notificationOutboxService.createNotification(
                            event.getEventId(),
                            "ORDER_CREATED",
                            event.getEmail(),
                            subject,
                            body);

            if (!created) {

                log.info(
                        "Notification already exists. Marking event as processed. "
                        + "eventId={}, orderId={}",
                        event.getEventId(),
                        event.getOrderId());

                eventIdempotencyService.markProcessed(
                        event.getEventId(),
                        "ORDER_CREATED");

                return;
            }

            boolean marked =
                    eventIdempotencyService.markProcessed(
                            event.getEventId(),
                            "ORDER_CREATED");

            if (!marked) {

                log.info(
                        "Event was already processed concurrently. "
                        + "eventId={}, orderId={}",
                        event.getEventId(),
                        event.getOrderId());

                return;
            }

            log.info(
                    "Order-created event processed successfully. "
                    + "eventId={}, orderId={}",
                    event.getEventId(),
                    event.getOrderId());

        } catch (Exception e) {

            log.error(
                    "Failed to process order-created event",
                    e);

            throw new RuntimeException(
                    "Failed to process order-created event",
                    e);
        }
    }
}