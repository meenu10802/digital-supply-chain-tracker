package com.supplychain.report.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelayedShipmentDto {
    private Long shipmentId;
    private String itemName;
    private String fromLocation;
    private String toLocation;
    private LocalDate expectedDelivery;
    private String currentStatus;
    private long daysOverdue;
}