package com.supplychain.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertTriggeredEvent {

    private String eventType;

    private String email;

    private String username;

    private String alertType;

    private String message;
}