package com.supplychain.shipment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDto implements Serializable {

    private Long id;
    private Long itemId;
    private String itemName;
    private Long supplierId;
    private Long transporterId;
    private String fromLocation;
    private String toLocation;
    private LocalDateTime expectedDelivery;
    private LocalDateTime actualDelivery;
    private String currentStatus;
    private String notes;
    private LocalDateTime createdAt;
}