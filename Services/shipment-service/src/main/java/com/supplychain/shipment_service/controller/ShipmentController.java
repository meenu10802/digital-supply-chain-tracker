package com.supplychain.shipment_service.controller;

import com.supplychain.shipment_service.dto.AssignTransporterRequestDto;
import com.supplychain.shipment_service.dto.CreateShipmentRequestDto;
import com.supplychain.shipment_service.dto.UpdateStatusRequestDto;
import com.supplychain.shipment_service.enums.ShipmentStatus;
import com.supplychain.shipment_service.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Operation(summary = "Create Shipment")
    @PostMapping
    public ResponseEntity<?> createShipment(

            @RequestBody CreateShipmentRequestDto request,

            @Parameter(description = "Supplier User ID")
            @RequestHeader("X-User-Id") Long supplierId
    ) {

        return ResponseEntity.ok(
                shipmentService.createShipment(
                        request,
                        supplierId
                )
        );
    }

    @Operation(summary = "Get All Shipments")
    @GetMapping
    public ResponseEntity<?> getAllShipments() {

        return ResponseEntity.ok(
                shipmentService.getAllShipments()
        );
    }

    @Operation(summary = "Get Shipment By ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getShipment(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                shipmentService.getShipmentById(id)
        );
    }

    @Operation(summary = "Assign Transporter")
    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTransporter(

            @PathVariable Long id,

            @RequestBody AssignTransporterRequestDto request
    ) {

        return ResponseEntity.ok(
                shipmentService.assignTransporter(
                        id,
                        request
                )
        );
    }

    @Operation(summary = "Update Shipment Status")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(

            @PathVariable Long id,

            @RequestBody UpdateStatusRequestDto request,

            @Parameter(description = "User Role")
            @RequestHeader("X-User-Roles") String role
    ) {

        return ResponseEntity.ok(
                shipmentService.updateStatus(
                        id,
                        request
                )
        );
    }

    @Operation(summary = "Confirm Shipment Receipt")
    @PutMapping("/{id}/receive")
    public ResponseEntity<?> receiveShipment(

            @PathVariable Long id,

            @Parameter(description = "User Role")
            @RequestHeader("X-User-Roles") String role
    ) {

        return ResponseEntity.ok(
                shipmentService.confirmReceipt(id)
        );
    }

    @Operation(summary = "Get Shipments By Supplier")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<?> getBySupplier(

            @PathVariable Long supplierId
    ) {

        return ResponseEntity.ok(
                shipmentService.getShipmentsBySupplier(
                        supplierId
                )
        );
    }

    @Operation(summary = "Get Shipments By Transporter")
    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<?> getByTransporter(

            @PathVariable Long transporterId
    ) {

        return ResponseEntity.ok(
                shipmentService.getShipmentsByTransporter(
                        transporterId
                )
        );
    }

    @Operation(summary = "Get Shipments By Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(

            @PathVariable ShipmentStatus status
    ) {

        return ResponseEntity.ok(
                shipmentService.getShipmentsByStatus(status)
        );
    }

    @Operation(summary = "Cancel Shipment")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelShipment(

            @PathVariable Long id
    ) {

        shipmentService.cancelShipment(id);

        return ResponseEntity.ok(
                "Shipment cancelled successfully"
        );
    }
}