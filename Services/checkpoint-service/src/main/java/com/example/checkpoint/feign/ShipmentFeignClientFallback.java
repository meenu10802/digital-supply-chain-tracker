// feign/ShipmentFeignClientFallback.java
package com.example.checkpoint.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShipmentFeignClientFallback implements ShipmentFeignClient {

    private static final Logger log = LoggerFactory.getLogger(ShipmentFeignClientFallback.class);

    @Override
    public ShipmentResponseDto getShipmentById(Long id) {
        log.warn("Fallback triggered for getShipmentById: {}", id);
        return null; // caller handles null as "not found"
    }
}