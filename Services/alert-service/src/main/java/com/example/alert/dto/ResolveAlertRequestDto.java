// dto/ResolveAlertRequestDto.java
package com.example.alert.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResolveAlertRequestDto {

    // Optional note explaining why the alert is being resolved
    private String resolutionNote;

    @NotNull(message = "Resolver user ID is required")
    private Long resolvedBy;
}