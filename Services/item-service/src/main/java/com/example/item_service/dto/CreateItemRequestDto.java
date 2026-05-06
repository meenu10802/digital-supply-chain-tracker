package com.example.item_service.dto;

import com.example.item_service.enums.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Category is required")
    private ItemCategory category;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Unit is required")
    private String unit;
}