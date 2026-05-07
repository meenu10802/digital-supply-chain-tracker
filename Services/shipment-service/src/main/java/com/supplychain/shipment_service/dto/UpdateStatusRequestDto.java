package com.supplychain.shipment_service.dto;

import com.supplychain.shipment_service.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {

    @NotNull
    private ShipmentStatus status;

    private String notes;

    private String location;
}