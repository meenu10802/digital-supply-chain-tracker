package com.supplychain.report.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPerformanceDto {
    private Long entityId;
    private String entityName;
    private String entityType;
    private long totalShipments;
    private long deliveredOnTime;
    private long delayed;
    private double onTimeRatePercent;
}