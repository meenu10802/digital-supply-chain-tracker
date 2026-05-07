package com.supplychain.shipment_service.service;

import com.supplychain.shipment_service.dto.AssignTransporterRequestDto;
import com.supplychain.shipment_service.dto.CreateShipmentRequestDto;
import com.supplychain.shipment_service.dto.ShipmentResponseDto;
import com.supplychain.shipment_service.dto.UpdateStatusRequestDto;
import com.supplychain.shipment_service.enums.ShipmentStatus;

import java.util.List;

public interface ShipmentService {

    ShipmentResponseDto createShipment(
            CreateShipmentRequestDto dto,
            Long supplierId
    );

    List<ShipmentResponseDto> getAllShipments();

    ShipmentResponseDto getShipmentById(Long id);

    ShipmentResponseDto assignTransporter(
            Long shipmentId,
            AssignTransporterRequestDto dto
    );

    ShipmentResponseDto updateStatus(
            Long shipmentId,
            UpdateStatusRequestDto dto
    );

    ShipmentResponseDto confirmReceipt(Long shipmentId);

    List<ShipmentResponseDto> getShipmentsBySupplier(Long supplierId);

    List<ShipmentResponseDto> getShipmentsByTransporter(Long transporterId);

    List<ShipmentResponseDto> getShipmentsByStatus(
            ShipmentStatus status
    );

    void cancelShipment(Long shipmentId);
}