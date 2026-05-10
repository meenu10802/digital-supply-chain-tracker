package com.supplychain.report.feign;

import com.supplychain.report.config.FeignClientConfig;
import com.supplychain.report.dto.ShipmentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "shipment-service", configuration = FeignClientConfig.class)
public interface ShipmentFeignClient {

    @GetMapping("/api/shipments")
    List<ShipmentResponseDto> getAllShipments();

    @GetMapping("/api/shipments/{id}")
    ShipmentResponseDto getShipmentById(@PathVariable Long id);

    @GetMapping("/api/shipments/status/{status}")
    List<ShipmentResponseDto> getByStatus(@PathVariable String status);

    @GetMapping("/api/shipments/supplier/{id}")
    List<ShipmentResponseDto> getBySupplier(@PathVariable Long id);

    @GetMapping("/api/shipments/transporter/{id}")
    List<ShipmentResponseDto> getByTransporter(@PathVariable Long id);
}