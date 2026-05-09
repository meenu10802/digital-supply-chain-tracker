// feign/ShipmentFeignClientFallback.java
package com.example.alert.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShipmentFeignClientFallback implements ShipmentFeignClient {

    @Override
    public ShipmentResponseDto getShipmentById(Long id) {
        log.warn("Fallback triggered — getShipmentById: {}", id);
        return null;
    }
}