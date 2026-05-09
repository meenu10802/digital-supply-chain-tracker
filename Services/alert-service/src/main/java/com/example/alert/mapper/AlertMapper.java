// mapper/AlertMapper.java
package com.example.alert.mapper;

import com.example.alert.dto.AlertResponseDto;
import com.example.alert.entity.Alert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    AlertResponseDto toResponseDto(Alert alert);
}