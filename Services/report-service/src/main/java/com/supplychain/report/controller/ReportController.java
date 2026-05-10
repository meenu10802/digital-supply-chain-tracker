package com.supplychain.report.controller;

import com.supplychain.report.dto.*;
import com.supplychain.report.generator.ReportGeneratorFactory;
import com.supplychain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports", description = "Supply chain delivery and performance reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportGeneratorFactory reportGeneratorFactory;

    @GetMapping("/delivery-performance")
    @Operation(summary = "Delivery on-time rate per supplier/transporter")
    public ResponseEntity<List<DeliveryPerformanceDto>> getDeliveryPerformance() {
        return ResponseEntity.ok(reportService.getDeliveryPerformance());
    }

    @GetMapping("/delayed-shipments")
    @Operation(summary = "All delayed shipments with delay duration")
    public ResponseEntity<List<DelayedShipmentDto>> getDelayedShipments() {
        return ResponseEntity.ok(reportService.getDelayedShipments());
    }

    @GetMapping("/inventory-status")
    @Operation(summary = "Active shipments at each location node")
    public ResponseEntity<List<ShipmentResponseDto>> getInventoryStatus() {
        return ResponseEntity.ok(reportService.getInventoryStatus());
    }

    @GetMapping("/supplier/{id}/summary")
    @Operation(summary = "Performance summary for a specific supplier")
    public ResponseEntity<DeliveryPerformanceDto> getSupplierSummary(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getSupplierSummary(id));
    }

    @GetMapping("/transporter/{id}/summary")
    @Operation(summary = "Performance summary for a specific transporter")
    public ResponseEntity<DeliveryPerformanceDto> getTransporterSummary(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getTransporterSummary(id));
    }

    @GetMapping("/export")
    @Operation(summary = "Export report as PDF or Excel",
            description = "type=pdf|excel, reportType=delay|performance|inventory")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String type,
            @RequestParam String reportType) throws Exception {

        byte[] data = reportService.exportReport(type, reportType);
        String ext = reportGeneratorFactory.getGenerator(type).getFileExtension();
        String contentType = reportGeneratorFactory.getGenerator(type).getContentType();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + reportType + "_report" + ext)
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }

    @GetMapping("/history")
    @Operation(summary = "Previously generated report metadata")
    public ResponseEntity<List<ReportMetadataDto>> getReportHistory() {
        return ResponseEntity.ok(reportService.getReportHistory());
    }
}