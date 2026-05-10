package com.supplychain.report.feign;

import com.supplychain.report.config.FeignClientConfig;
import com.supplychain.report.dto.CheckpointLogResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "checkpoint-service", configuration = FeignClientConfig.class)
public interface CheckpointFeignClient {

    @GetMapping("/api/checkpoints/shipment/{id}")
    List<CheckpointLogResponseDto> getTimeline(@PathVariable Long id);
}