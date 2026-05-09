package com.example.alert.controller;

import com.example.alert.dto.AlertResponseDto;
import com.example.alert.dto.ResolveAlertRequestDto;
import com.example.alert.enums.AlertType;
import com.example.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Supply chain delay and damage alert management")
@SecurityRequirement(name = "bearerAuth")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    @Operation(summary = "List all alerts — paginated (ADMIN)")
    public ResponseEntity<Page<AlertResponseDto>> getAllAlerts(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "ADMIN"
            )
            @RequestHeader("X-User-Roles") String roles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (isNotAdmin(roles)) {
            return forbidden();
        }

        Page<AlertResponseDto> result = alertService.getAllAlerts(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert by ID (ADMIN)")
    public ResponseEntity<AlertResponseDto> getById(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "ADMIN"
            )
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable Long id) {

        if (isNotAdmin(roles)) {
            return forbidden();
        }

        return ResponseEntity.ok(alertService.getById(id));
    }

    @GetMapping("/shipment/{shipmentId}")
    @Operation(summary = "Alerts for a specific shipment (ADMIN / SUPPLIER)")
    public ResponseEntity<List<AlertResponseDto>> getByShipmentId(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "SUPPLIER"
            )
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable Long shipmentId) {

        if (!hasAllowedShipmentRole(roles)) {
            return forbidden();
        }

        return ResponseEntity.ok(alertService.getByShipmentId(shipmentId));
    }

    @GetMapping("/unresolved")
    @Operation(summary = "All unresolved alerts (ADMIN)")
    public ResponseEntity<List<AlertResponseDto>> getUnresolved(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "ADMIN"
            )
            @RequestHeader("X-User-Roles") String roles) {

        if (isNotAdmin(roles)) {
            return forbidden();
        }

        return ResponseEntity.ok(alertService.getUnresolved());
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Mark alert as resolved (ADMIN)")
    public ResponseEntity<AlertResponseDto> resolveAlert(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "ADMIN"
            )
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable Long id,
            @Valid @RequestBody ResolveAlertRequestDto dto) {

        if (isNotAdmin(roles)) {
            return forbidden();
        }

        return ResponseEntity.ok(alertService.resolveAlert(id, dto));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Alerts filtered by type (ADMIN)")
    public ResponseEntity<List<AlertResponseDto>> getByType(
            @Parameter(
                    name = "X-User-Roles",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "User roles passed from gateway",
                    example = "ADMIN"
            )
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable AlertType type) {

        if (isNotAdmin(roles)) {
            return forbidden();
        }

        return ResponseEntity.ok(alertService.getByType(type));
    }

    private boolean isNotAdmin(String roles) {
        return roles == null || !roles.contains("ADMIN");
    }

    private boolean hasAllowedShipmentRole(String roles) {
        return roles != null && (roles.contains("ADMIN") || roles.contains("SUPPLIER"));
    }

    private <T> ResponseEntity<T> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}