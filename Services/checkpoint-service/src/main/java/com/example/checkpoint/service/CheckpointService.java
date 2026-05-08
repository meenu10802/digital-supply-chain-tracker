// service/CheckpointService.java
package com.example.checkpoint.service;

import com.example.checkpoint.dto.AddCheckpointRequestDto;
import com.example.checkpoint.dto.CheckpointLogResponseDto;
import com.example.checkpoint.entity.CheckpointLog;
import com.example.checkpoint.enums.CheckpointStatus;
import com.example.checkpoint.exception.ResourceNotFoundException;
import com.example.checkpoint.feign.ShipmentFeignClient;
import com.example.checkpoint.feign.ShipmentResponseDto;
import com.example.checkpoint.mapper.CheckpointMapper;
import com.example.checkpoint.repository.CheckpointLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckpointService {

    private final CheckpointLogRepository repository;
    private final CheckpointMapper mapper;
    private final ShipmentFeignClient shipmentFeignClient;

    @Transactional
    public CheckpointLogResponseDto addCheckpoint(AddCheckpointRequestDto dto,
                                                  Long userId, String userName) {
        // Validate shipment exists via Feign
        ShipmentResponseDto shipment = shipmentFeignClient.getShipmentById(dto.getShipmentId());
        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment not found with id: " + dto.getShipmentId());
        }

        LocalDateTime ts = (dto.getTimestamp() != null) ? dto.getTimestamp() : LocalDateTime.now();

        CheckpointLog checkpointLog = CheckpointLog.builder()
                .shipmentId(dto.getShipmentId())
                .location(dto.getLocation())
                .checkpointStatus(dto.getCheckpointStatus())
                .notes(dto.getNotes())
                .loggedByUserId(userId)
                .loggedByName(userName)
                .timestamp(ts)
                .build();

        CheckpointLog saved = repository.save(checkpointLog);
        log.info("Checkpoint added — shipmentId={} status={} by userId={}",
                dto.getShipmentId(), dto.getCheckpointStatus(), userId);
        return mapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CheckpointLogResponseDto> getTimeline(Long shipmentId) {
        return repository.findTimelineByShipment(shipmentId)
                .stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CheckpointLogResponseDto getById(Long id) {
        CheckpointLog checkpointLog = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checkpoint not found: " + id));
        return mapper.toResponseDto(checkpointLog);
    }

    @Transactional(readOnly = true)
    public CheckpointLogResponseDto getLatest(Long shipmentId) {
        return repository.findLatestByShipment(shipmentId)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No checkpoints found for shipment: " + shipmentId));
    }

    @Transactional(readOnly = true)
    public List<CheckpointLogResponseDto> getByLocation(String location) {
        return repository.findByLocation(location)
                .stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Called by RabbitMQ consumer — auto-log status transitions
    @Transactional
    public void autoLogFromEvent(Long shipmentId, String statusStr,
                                 String location, Long triggeredByUserId) {
        CheckpointStatus status;
        try {
            status = CheckpointStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            log.warn("Cannot map shipment status '{}' to CheckpointStatus — skipping auto-log", statusStr);
            return;
        }

        CheckpointLog autoLog = CheckpointLog.builder()
                .shipmentId(shipmentId)
                .location(location != null ? location : "System")
                .checkpointStatus(status)
                .notes("Auto-logged via status change event")
                .loggedByUserId(triggeredByUserId != null ? triggeredByUserId : 0L)
                .loggedByName("System")
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(autoLog);
        log.info("Auto-checkpoint created — shipmentId={} status={}", shipmentId, status);
    }
}