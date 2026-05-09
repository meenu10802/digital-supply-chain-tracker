// feign/ShipmentFeignClient.java
package com.example.alert.feign;

import com.example.alert.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "shipment-service",
        configuration = FeignClientConfig.class,
        fallback = ShipmentFeignClientFallback.class
)
public interface ShipmentFeignClient {

    @GetMapping("/api/shipments/{id}")
    ShipmentResponseDto getShipmentById(@PathVariable("id") Long id);
}