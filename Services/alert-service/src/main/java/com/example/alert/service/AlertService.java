// service/AlertService.java
package com.example.alert.service;

import com.example.alert.dto.AlertResponseDto;
import com.example.alert.dto.ResolveAlertRequestDto;
import com.example.alert.entity.Alert;
import com.example.alert.enums.AlertType;
import com.example.alert.exception.ResourceNotFoundException;
import com.example.alert.feign.ShipmentFeignClient;
import com.example.alert.feign.ShipmentResponseDto;
import com.example.alert.mapper.AlertMapper;
import com.example.alert.messaging.AlertEventPublisher;
import com.example.alert.repository.AlertRepository;
import com.example.alert.service.strategy.AlertHandler;
import com.example.alert.service.strategy.DamageAlertHandler;
import com.example.alert.service.strategy.DelayAlertHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final ShipmentFeignClient shipmentFeignClient;
    private final AlertEventPublisher alertEventPublisher;
    private final DelayAlertHandler delayAlertHandler;
    private final DamageAlertHandler damageAlertHandler;

    // ----------------------------------------------------------------
    // Called by RabbitMQ consumers
    // ----------------------------------------------------------------

    @Transactional
    public void processDelayEvent(Map<String, Object> payload) {
        String shipmentReference = resolveShipmentReference(payload);
        Alert alert = delayAlertHandler.handle(payload, shipmentReference);
        Alert saved = alertRepository.save(alert);
        log.info("DELAY alert created — alertId={} shipmentId={}", saved.getId(), saved.getShipmentId());
        alertEventPublisher.publishAlertTriggered(saved);
    }

    @Transactional
    public void processDamageEvent(Map<String, Object> payload) {
        String shipmentReference = resolveShipmentReference(payload);
        Alert alert = damageAlertHandler.handle(payload, shipmentReference);
        Alert saved = alertRepository.save(alert);
        log.info("DAMAGE alert created — alertId={} shipmentId={}", saved.getId(), saved.getShipmentId());
        alertEventPublisher.publishAlertTriggered(saved);
    }

    // ----------------------------------------------------------------
    // REST — read operations
    // ----------------------------------------------------------------

    @Transactional(readOnly = true)
    public Page<AlertResponseDto> getAllAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable).map(alertMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public AlertResponseDto getById(Long id) {
        Alert alert = findOrThrow(id);
        return alertMapper.toResponseDto(alert);
    }

    @Transactional(readOnly = true)
    public List<AlertResponseDto> getByShipmentId(Long shipmentId) {
        return alertRepository.findByShipmentIdOrdered(shipmentId)
                .stream().map(alertMapper::toResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlertResponseDto> getUnresolved() {
        return alertRepository.findAllUnresolved()
                .stream().map(alertMapper::toResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlertResponseDto> getByType(AlertType type) {
        return alertRepository.findByAlertType(type)
                .stream().map(alertMapper::toResponseDto).collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // REST — resolve
    // ----------------------------------------------------------------

    @Transactional
    public AlertResponseDto resolveAlert(Long id, ResolveAlertRequestDto dto) {
        Alert alert = findOrThrow(id);

        if (Boolean.TRUE.equals(alert.getIsResolved())) {
            throw new IllegalStateException("Alert " + id + " is already resolved.");
        }

        alert.setIsResolved(true);
        alert.setResolvedBy(dto.getResolvedBy());
        alert.setResolvedAt(LocalDateTime.now());

        Alert saved = alertRepository.save(alert);
        log.info("Alert resolved — alertId={} resolvedBy={}", id, dto.getResolvedBy());
        return alertMapper.toResponseDto(saved);
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private Alert findOrThrow(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + id));
    }

    private String resolveShipmentReference(Map<String, Object> payload) {
        try {
            Long shipmentId = Long.valueOf(payload.get("shipmentId").toString());
            ShipmentResponseDto shipment = shipmentFeignClient.getShipmentById(shipmentId);
            if (shipment != null) {
                return shipment.getItemName() + " (" + shipment.getFromLocation()
                        + " → " + shipment.getToLocation() + ")";
            }
        } catch (Exception e) {
            log.warn("Could not enrich alert with shipment details: {}", e.getMessage());
        }
        return null;
    }
}