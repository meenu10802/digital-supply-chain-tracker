package com.supplychain.notification_service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private int status;

    private String message;

    private Map<String, String> errors;

    private LocalDateTime timestamp;
}