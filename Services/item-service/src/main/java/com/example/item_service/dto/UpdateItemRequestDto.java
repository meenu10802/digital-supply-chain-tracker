package com.example.item_service.dto;
import com.example.item_service.enums.ItemCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateItemRequestDto {

    private String name;
    private String description;
    private ItemCategory category;
    private String unit;
}