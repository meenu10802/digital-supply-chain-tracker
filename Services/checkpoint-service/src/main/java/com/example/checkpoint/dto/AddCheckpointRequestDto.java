// dto/AddCheckpointRequestDto.java
package com.example.checkpoint.dto;

import com.example.checkpoint.enums.CheckpointStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddCheckpointRequestDto {

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Checkpoint status is required")
    private CheckpointStatus checkpointStatus;

    private String notes;

    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;
}