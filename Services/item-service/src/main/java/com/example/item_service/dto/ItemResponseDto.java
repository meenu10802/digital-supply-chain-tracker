package com.example.item_service.dto;

import com.example.item_service.enums.ItemCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto {

    private Long id;
    private String name;
    private String description;
    private ItemCategory category;
    private Long supplierId;
    private String sku;
    private String unit;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
