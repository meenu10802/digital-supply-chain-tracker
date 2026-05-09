package com.supplychain.notification_service.dto;

import com.supplychain.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequestDto {

    private String recipientEmail;

    private String subject;

    private String message;

    private NotificationType notificationType;
}