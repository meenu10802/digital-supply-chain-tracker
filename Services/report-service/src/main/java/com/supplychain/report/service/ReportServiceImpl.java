package com.supplychain.report.service;

import com.supplychain.report.dto.*;
import com.supplychain.report.entity.ReportMetadata;
import com.supplychain.report.feign.*;
import com.supplychain.report.generator.ReportGenerator;
import com.supplychain.report.generator.ReportGeneratorFactory;
import com.supplychain.report.repository.ReportMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ShipmentFeignClient shipmentFeignClient;
    private final ItemFeignClient itemFeignClient;
    private final CheckpointFeignClient checkpointFeignClient;
    private final ReportGeneratorFactory reportGeneratorFactory;
    private final ReportMetadataRepository reportMetadataRepository;

    @Override
    public List<DeliveryPerformanceDto> getDeliveryPerformance() {
        log.info("Generating delivery performance report");
        List<ShipmentResponseDto> all = shipmentFeignClient.getAllShipments();

        return all.stream()
                .collect(Collectors.groupingBy(s -> s.getSupplierId()))
                .entrySet().stream()
                .map(entry -> {
                    Long supplierId = entry.getKey();
                    List<ShipmentResponseDto> shipments = entry.getValue();
                    long total = shipments.size();
                    long onTime = shipments.stream()
                            .filter(s -> "DELIVERED".equals(s.getCurrentStatus())
                                    && s.getActualDelivery() != null
                                    && !s.getActualDelivery().isAfter(s.getExpectedDelivery()))
                            .count();
                    long delayed = shipments.stream()
                            .filter(s -> "DELAYED".equals(s.getCurrentStatus()))
                            .count();
                    String name = shipments.get(0).getSupplierName();
                    return DeliveryPerformanceDto.builder()
                            .entityId(supplierId)
                            .entityName(name)
                            .entityType("SUPPLIER")
                            .totalShipments(total)
                            .deliveredOnTime(onTime)
                            .delayed(delayed)
                            .onTimeRatePercent(total > 0 ? (onTime * 100.0 / total) : 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DelayedShipmentDto> getDelayedShipments() {
        log.info("Fetching all delayed shipments");
        return shipmentFeignClient.getByStatus("DELAYED").stream()
                .map(s -> DelayedShipmentDto.builder()
                        .shipmentId(s.getId())
                        .itemName(s.getItemName())
                        .fromLocation(s.getFromLocation())
                        .toLocation(s.getToLocation())
                        .expectedDelivery(s.getExpectedDelivery())
                        .currentStatus(s.getCurrentStatus())
                        .daysOverdue(s.getExpectedDelivery() != null
                                ? ChronoUnit.DAYS.between(s.getExpectedDelivery(), LocalDate.now())
                                : 0)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponseDto> getInventoryStatus() {
        log.info("Fetching inventory status");
        return shipmentFeignClient.getAllShipments().stream()
                .filter(s -> !"DELIVERED".equals(s.getCurrentStatus())
                        && !"CANCELLED".equals(s.getCurrentStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryPerformanceDto getSupplierSummary(Long supplierId) {
        log.info("Generating supplier summary for supplierId={}", supplierId);
        List<ShipmentResponseDto> shipments = shipmentFeignClient.getBySupplier(supplierId);
        return buildPerformanceDto(supplierId, "SUPPLIER",
                shipments.isEmpty() ? "Unknown" : shipments.get(0).getSupplierName(),
                shipments);
    }

    @Override
    public DeliveryPerformanceDto getTransporterSummary(Long transporterId) {
        log.info("Generating transporter summary for transporterId={}", transporterId);
        List<ShipmentResponseDto> shipments = shipmentFeignClient.getByTransporter(transporterId);
        return buildPerformanceDto(transporterId, "TRANSPORTER", "Transporter " + transporterId, shipments);
    }

    @Override
    @Transactional
    public byte[] exportReport(String type, String reportType) throws Exception {
        log.info("Exporting report: type={}, reportType={}", type, reportType);
        List<?> data = switch (reportType.toLowerCase()) {
            case "delay" -> getDelayedShipments();
            case "performance" -> getDeliveryPerformance();
            case "inventory" -> getInventoryStatus();
            default -> throw new IllegalArgumentException("Unknown reportType: " + reportType);
        };

        ReportGenerator generator = reportGeneratorFactory.getGenerator(type);
        byte[] bytes = generator.generate(reportType, data);

        reportMetadataRepository.save(ReportMetadata.builder()
                .reportType(reportType)
                .format(type)
                .generatedAt(LocalDateTime.now())
                .description("Exported " + reportType + " report as " + type)
                .build());

        return bytes;
    }

    @Override
    public List<ReportMetadataDto> getReportHistory() {
        return reportMetadataRepository.findAllByOrderByGeneratedAtDesc()
                .stream()
                .map(r -> ReportMetadataDto.builder()
                        .id(r.getId())
                        .reportType(r.getReportType())
                        .format(r.getFormat())
                        .generatedByUserId(r.getGeneratedByUserId())
                        .generatedAt(r.getGeneratedAt())
                        .description(r.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    private DeliveryPerformanceDto buildPerformanceDto(Long id, String type,
                                                       String name, List<ShipmentResponseDto> shipments) {
        long total = shipments.size();
        long onTime = shipments.stream()
                .filter(s -> "DELIVERED".equals(s.getCurrentStatus())
                        && s.getActualDelivery() != null
                        && !s.getActualDelivery().isAfter(s.getExpectedDelivery()))
                .count();
        long delayed = shipments.stream()
                .filter(s -> "DELAYED".equals(s.getCurrentStatus()))
                .count();
        return DeliveryPerformanceDto.builder()
                .entityId(id)
                .entityName(name)
                .entityType(type)
                .totalShipments(total)
                .deliveredOnTime(onTime)
                .delayed(delayed)
                .onTimeRatePercent(total > 0 ? (onTime * 100.0 / total) : 0)
                .build();
    }
}