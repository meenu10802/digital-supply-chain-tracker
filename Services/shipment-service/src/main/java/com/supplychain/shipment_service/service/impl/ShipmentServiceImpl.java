package com.supplychain.shipment_service.service.impl;

import com.supplychain.shipment_service.dto.AssignTransporterRequestDto;
import com.supplychain.shipment_service.dto.CreateShipmentRequestDto;
import com.supplychain.shipment_service.dto.ShipmentResponseDto;
import com.supplychain.shipment_service.dto.UpdateStatusRequestDto;
import com.supplychain.shipment_service.entity.Shipment;
import com.supplychain.shipment_service.enums.ShipmentStatus;
import com.supplychain.shipment_service.feign.ItemFeignClient;
import com.supplychain.shipment_service.repository.ShipmentRepository;
import com.supplychain.shipment_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ItemFeignClient itemFeignClient;

    @Override
    public ShipmentResponseDto createShipment(
            CreateShipmentRequestDto dto,
            Long supplierId
    ) {

        Map<String, Object> item =
                itemFeignClient.getItemById(dto.getItemId());

        Shipment shipment = Shipment.builder()
                .itemId(dto.getItemId())
                .itemName(item.get("name").toString())
                .supplierId(supplierId)
                .fromLocation(dto.getFromLocation())
                .toLocation(dto.getToLocation())
                .expectedDelivery(dto.getExpectedDelivery())
                .notes(dto.getNotes())
                .currentStatus(ShipmentStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToDto(shipmentRepository.save(shipment));
    }

    @Override
    public List<ShipmentResponseDto> getAllShipments() {

        return shipmentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Cacheable(
            value = "shipmentStatus",
            key = "#id"
    )
    public ShipmentResponseDto getShipmentById(Long id) {

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        return mapToDto(shipment);
    }

    @Override
    public ShipmentResponseDto assignTransporter(
            Long shipmentId,
            AssignTransporterRequestDto dto
    ) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        shipment.setTransporterId(dto.getTransporterId());
        shipment.setCurrentStatus(ShipmentStatus.ASSIGNED);
        shipment.setUpdatedAt(LocalDateTime.now());

        return mapToDto(shipmentRepository.save(shipment));
    }

    @Override
    @CachePut(
            value = "shipmentStatus",
            key = "#shipmentId"
    )
    public ShipmentResponseDto updateStatus(
            Long shipmentId,
            UpdateStatusRequestDto dto
    ) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        shipment.setCurrentStatus(dto.getStatus());
        shipment.setNotes(dto.getNotes());
        shipment.setUpdatedAt(LocalDateTime.now());

        if (dto.getStatus() == ShipmentStatus.DELIVERED) {
            shipment.setActualDelivery(LocalDateTime.now());
        }

        return mapToDto(shipmentRepository.save(shipment));
    }

    @Override
    public ShipmentResponseDto confirmReceipt(Long shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        shipment.setCurrentStatus(ShipmentStatus.DELIVERED);
        shipment.setActualDelivery(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());

        return mapToDto(shipmentRepository.save(shipment));
    }

    @Override
    public List<ShipmentResponseDto> getShipmentsBySupplier(
            Long supplierId
    ) {

        return shipmentRepository.findBySupplierId(supplierId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ShipmentResponseDto> getShipmentsByTransporter(
            Long transporterId
    ) {

        return shipmentRepository.findByTransporterId(transporterId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ShipmentResponseDto> getShipmentsByStatus(
            ShipmentStatus status
    ) {

        return shipmentRepository.findByCurrentStatus(status)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void cancelShipment(Long shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        if (shipment.getCurrentStatus() != ShipmentStatus.CREATED) {
            throw new RuntimeException(
                    "Only CREATED shipments can be cancelled"
            );
        }

        shipment.setCurrentStatus(ShipmentStatus.CANCELLED);
        shipment.setUpdatedAt(LocalDateTime.now());

        shipmentRepository.save(shipment);
    }

    private ShipmentResponseDto mapToDto(Shipment shipment) {

        return ShipmentResponseDto.builder()
                .id(shipment.getId())
                .itemId(shipment.getItemId())
                .itemName(shipment.getItemName())
                .supplierId(shipment.getSupplierId())
                .transporterId(shipment.getTransporterId())
                .fromLocation(shipment.getFromLocation())
                .toLocation(shipment.getToLocation())
                .expectedDelivery(shipment.getExpectedDelivery())
                .actualDelivery(shipment.getActualDelivery())
                .currentStatus(shipment.getCurrentStatus().name())
                .notes(shipment.getNotes())
                .createdAt(shipment.getCreatedAt())
                .build();
    }
}