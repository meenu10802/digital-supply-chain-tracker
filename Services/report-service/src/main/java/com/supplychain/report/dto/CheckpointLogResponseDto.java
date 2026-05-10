package com.supplychain.report.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckpointLogResponseDto {
    private Long id;
    private Long shipmentId;
    private String location;
    private String checkpointStatus;
    private String notes;
    private Long loggedByUserId;
    private String loggedByName;
    private LocalDateTime timestamp;
}