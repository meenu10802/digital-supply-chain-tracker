package com.supplychain.notification_service.messaging;

import com.supplychain.notification_service.config.RabbitMQConfig;
import com.supplychain.notification_service.dto.EmailRequestDto;
import com.supplychain.notification_service.entity.NotificationLog;
import com.supplychain.notification_service.enums.NotificationStatus;
import com.supplychain.notification_service.enums.NotificationType;
import com.supplychain.notification_service.event.AlertTriggeredEvent;
import com.supplychain.notification_service.event.ShipmentCreatedEvent;
import com.supplychain.notification_service.event.ShipmentStatusChangedEvent;
import com.supplychain.notification_service.event.UserRegisteredEvent;
import com.supplychain.notification_service.repository.NotificationLogRepository;
import com.supplychain.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationLogRepository notificationLogRepository;

    private final EmailService emailService;

    // =========================================================
    // USER REGISTERED EVENT
    // =========================================================

    @RabbitListener(
            queues = RabbitMQConfig.USER_QUEUE
    )
    public void consumeUserRegisteredEvent(
            UserRegisteredEvent event
    ) {

        try {

            log.info(
                    "Received user registered event: {}",
                    event
            );

            String subject =
                    "Welcome to Supply Chain Tracker";

            String message =
                    "Hello " + event.getUsername()
                            + ",\n\nYour account has been created successfully."
                            + "\nRole: " + event.getRole();

            processNotification(
                    event.getEmail(),
                    subject,
                    message,
                    NotificationType.USER_REGISTERED
            );

        } catch (Exception ex) {

            log.error(
                    "User registered notification failed",
                    ex
            );

            throw ex;
        }
    }

    // =========================================================
    // SHIPMENT CREATED EVENT
    // =========================================================

    @RabbitListener(
            queues = RabbitMQConfig.SHIPMENT_QUEUE
    )
    public void consumeShipmentCreatedEvent(
            ShipmentCreatedEvent event
    ) {

        try {

            log.info(
                    "Received shipment created event: {}",
                    event
            );

            String subject =
                    "Shipment Created Successfully";

            String message =
                    "Hello " + event.getUsername()
                            + ",\n\nShipment ID: "
                            + event.getShipmentId()
                            + "\nStatus: "
                            + event.getStatus();

            processNotification(
                    event.getEmail(),
                    subject,
                    message,
                    NotificationType.SHIPMENT_CREATED
            );

        } catch (Exception ex) {

            log.error(
                    "Shipment created notification failed",
                    ex
            );

            throw ex;
        }
    }

    // =========================================================
    // SHIPMENT STATUS CHANGED EVENT
    // =========================================================

    @RabbitListener(
            queues = RabbitMQConfig.STATUS_QUEUE
    )
    public void consumeShipmentStatusChangedEvent(
            ShipmentStatusChangedEvent event
    ) {

        try {

            log.info(
                    "Received shipment status changed event: {}",
                    event
            );

            String subject =
                    "Shipment Status Updated";

            String message =
                    "Hello " + event.getUsername()
                            + ",\n\nShipment ID: "
                            + event.getShipmentId()
                            + "\nOld Status: "
                            + event.getOldStatus()
                            + "\nNew Status: "
                            + event.getNewStatus();

            processNotification(
                    event.getEmail(),
                    subject,
                    message,
                    NotificationType.SHIPMENT_STATUS_CHANGED
            );

        } catch (Exception ex) {

            log.error(
                    "Shipment status notification failed",
                    ex
            );

            throw ex;
        }
    }

    // =========================================================
    // ALERT TRIGGERED EVENT
    // =========================================================

    @RabbitListener(
            queues = RabbitMQConfig.ALERT_QUEUE
    )
    public void consumeAlertTriggeredEvent(
            AlertTriggeredEvent event
    ) {

        try {

            log.info(
                    "Received alert event: {}",
                    event
            );

            String subject =
                    "Supply Chain Alert";

            String message =
                    "Hello " + event.getUsername()
                            + ",\n\nAlert Type: "
                            + event.getAlertType()
                            + "\nMessage: "
                            + event.getMessage();

            processNotification(
                    event.getEmail(),
                    subject,
                    message,
                    NotificationType.ALERT_TRIGGERED
            );

        } catch (Exception ex) {

            log.error(
                    "Alert notification failed",
                    ex
            );

            throw ex;
        }
    }

    // =========================================================
    // COMMON NOTIFICATION PROCESSOR
    // =========================================================

    private void processNotification(
            String recipientEmail,
            String subject,
            String message,
            NotificationType notificationType
    ) {

        EmailRequestDto emailRequestDto =
                EmailRequestDto.builder()
                        .recipientEmail(recipientEmail)
                        .subject(subject)
                        .message(message)
                        .notificationType(notificationType)
                        .build();

        // Send Email
        emailService.sendEmail(emailRequestDto);

        // Save Notification Log
        NotificationLog notificationLog =
                NotificationLog.builder()
                        .recipientEmail(recipientEmail)
                        .subject(subject)
                        .message(message)
                        .notificationType(notificationType)
                        .status(NotificationStatus.SENT)
                        .isRead(false)
                        .sentAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build();

        notificationLogRepository.save(notificationLog);

        log.info(
                "Notification processed successfully for {}",
                recipientEmail
        );
    }
}