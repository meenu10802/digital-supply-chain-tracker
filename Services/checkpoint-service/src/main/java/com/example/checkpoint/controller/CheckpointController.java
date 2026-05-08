package com.example.checkpoint.controller;

import com.example.checkpoint.dto.AddCheckpointRequestDto;
import com.example.checkpoint.dto.CheckpointLogResponseDto;
import com.example.checkpoint.service.CheckpointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
@Tag(name = "Checkpoints", description = "Checkpoint logs and event timeline per shipment")
@SecurityRequirement(name = "basicAuth")
public class CheckpointController {

    private final CheckpointService checkpointService;

    @PostMapping
    @Operation(summary = "Add a checkpoint log")
    public ResponseEntity<CheckpointLogResponseDto> addCheckpoint(
            @Valid @RequestBody AddCheckpointRequestDto dto) {

        CheckpointLogResponseDto response =
                checkpointService.addCheckpoint(dto, 1L, "admin");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/shipment/{shipmentId}")
    @Operation(summary = "Full event timeline for a shipment")
    public ResponseEntity<List<CheckpointLogResponseDto>> getTimeline(
            @PathVariable Long shipmentId) {

        return ResponseEntity.ok(
                checkpointService.getTimeline(shipmentId)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific checkpoint log by ID")
    public ResponseEntity<CheckpointLogResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                checkpointService.getById(id)
        );
    }

    @GetMapping("/shipment/{shipmentId}/latest")
    @Operation(summary = "Latest checkpoint for a shipment")
    public ResponseEntity<CheckpointLogResponseDto> getLatest(
            @PathVariable Long shipmentId) {

        return ResponseEntity.ok(
                checkpointService.getLatest(shipmentId)
        );
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Checkpoint logs by location")
    public ResponseEntity<List<CheckpointLogResponseDto>> getByLocation(
            @PathVariable String location) {

        return ResponseEntity.ok(
                checkpointService.getByLocation(location)
        );
    }
}