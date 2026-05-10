package com.supplychain.report.feign;

import com.supplychain.report.config.FeignClientConfig;
import com.supplychain.report.dto.ItemResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "item-service", configuration = FeignClientConfig.class)
public interface ItemFeignClient {

    @GetMapping("/api/items/supplier/{id}")
    List<ItemResponseDto> getItemsBySupplier(@PathVariable Long id);

    @GetMapping("/api/items")
    List<ItemResponseDto> getAllItems();
}