// dto/AlertResponseDto.java
package com.example.alert.dto;

import com.example.alert.enums.AlertSeverity;
import com.example.alert.enums.AlertType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlertResponseDto {
    private Long id;
    private Long shipmentId;
    private String shipmentReference;
    private AlertType alertType;
    private String message;
    private AlertSeverity severity;
    private Boolean isResolved;
    private Long resolvedBy;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
}