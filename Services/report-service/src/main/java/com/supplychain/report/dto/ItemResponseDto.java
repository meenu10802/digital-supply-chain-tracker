package com.supplychain.report.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private String category;
    private Long supplierId;
    private String supplierName;
    private String sku;
    private String unit;
    private boolean isActive;
}