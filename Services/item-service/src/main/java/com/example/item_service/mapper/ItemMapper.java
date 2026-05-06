package com.example.item_service.mapper;
import com.example.item_service.dto.CreateItemRequestDto;
import com.example.item_service.dto.ItemResponseDto;
import com.example.item_service.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplierId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Item toEntity(CreateItemRequestDto dto);

    ItemResponseDto toResponseDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplierId", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(
            com.example.item_service.dto.UpdateItemRequestDto dto,
            @MappingTarget Item item
    );
}
