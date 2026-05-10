package com.supplychain.report.service;

import com.supplychain.report.dto.*;
import java.util.List;

public interface ReportService {
    List<DeliveryPerformanceDto> getDeliveryPerformance();
    List<DelayedShipmentDto> getDelayedShipments();
    List<ShipmentResponseDto> getInventoryStatus();
    DeliveryPerformanceDto getSupplierSummary(Long supplierId);
    DeliveryPerformanceDto getTransporterSummary(Long transporterId);
    byte[] exportReport(String type, String reportType) throws Exception;
    List<ReportMetadataDto> getReportHistory();
}