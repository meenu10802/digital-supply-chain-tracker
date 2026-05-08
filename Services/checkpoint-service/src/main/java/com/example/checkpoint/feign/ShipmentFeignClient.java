// feign/ShipmentFeignClient.java
package com.example.checkpoint.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "shipment-service",
        configuration = com.example.checkpoint.config.FeignClientConfig.class,
        fallback = ShipmentFeignClientFallback.class
)
public interface ShipmentFeignClient {

    @GetMapping("/api/shipments/{id}")
    ShipmentResponseDto getShipmentById(@PathVariable("id") Long id);
}