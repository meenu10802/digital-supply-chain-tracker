package com.supplychain.report.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponseDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private Long supplierId;
    private String supplierName;
    private Long transporterId;
    private String fromLocation;
    private String toLocation;
    private LocalDate expectedDelivery;
    private LocalDate actualDelivery;
    private String currentStatus;
    private String notes;
    private LocalDateTime createdAt;
}