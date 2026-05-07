package com.supplychain.shipment_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateShipmentRequestDto {

    @NotNull
    private Long itemId;

    @NotBlank
    private String fromLocation;

    @NotBlank
    private String toLocation;

    @Future
    @NotNull
    private LocalDateTime expectedDelivery;

    private String notes;
}