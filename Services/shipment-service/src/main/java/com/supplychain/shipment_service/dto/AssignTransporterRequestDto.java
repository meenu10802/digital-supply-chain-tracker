package com.supplychain.shipment_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTransporterRequestDto {

    @NotNull
    private Long transporterId;
}