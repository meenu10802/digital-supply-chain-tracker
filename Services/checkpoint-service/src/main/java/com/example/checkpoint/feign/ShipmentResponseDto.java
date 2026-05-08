// feign/ShipmentResponseDto.java
package com.example.checkpoint.feign;

import lombok.Data;

@Data
public class ShipmentResponseDto {
    private Long id;
    private String currentStatus;
    private Long supplierId;
    private Long transporterId;
    private String fromLocation;
    private String toLocation;
}