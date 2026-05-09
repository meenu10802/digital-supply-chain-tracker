package com.supplychain.notification_service.dto;

import com.supplychain.notification_service.enums.NotificationStatus;
import com.supplychain.notification_service.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;

    private String recipientEmail;

    private String subject;

    private String message;

    private NotificationType notificationType;

    private NotificationStatus status;

    private Boolean isRead;

    private LocalDateTime sentAt;

    private LocalDateTime createdAt;
}