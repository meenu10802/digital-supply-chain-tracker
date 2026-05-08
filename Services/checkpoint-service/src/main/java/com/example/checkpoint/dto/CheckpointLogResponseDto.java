// dto/CheckpointLogResponseDto.java
package com.example.checkpoint.dto;

import com.example.checkpoint.enums.CheckpointStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckpointLogResponseDto {
    private Long id;
    private Long shipmentId;
    private String location;
    private CheckpointStatus checkpointStatus;
    private String notes;
    private Long loggedByUserId;
    private String loggedByName;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}