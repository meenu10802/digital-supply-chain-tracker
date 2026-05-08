package com.example.checkpoint.mapper;

import com.example.checkpoint.dto.CheckpointLogResponseDto;
import com.example.checkpoint.entity.CheckpointLog;
import org.springframework.stereotype.Component;

@Component
public class CheckpointMapper {

    public CheckpointLogResponseDto toResponseDto(CheckpointLog log) {
        return CheckpointLogResponseDto.builder()
                .id(log.getId())
                .shipmentId(log.getShipmentId())
                .location(log.getLocation())
                .checkpointStatus(log.getCheckpointStatus())
                .notes(log.getNotes())
                .loggedByUserId(log.getLoggedByUserId())
                .loggedByName(log.getLoggedByName())
                .timestamp(log.getTimestamp())
                .createdAt(log.getCreatedAt())
                .build();
    }
}