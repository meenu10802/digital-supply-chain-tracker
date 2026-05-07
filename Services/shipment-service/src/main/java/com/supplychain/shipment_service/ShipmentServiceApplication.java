package com.supplychain.shipment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableCaching
public class ShipmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ShipmentServiceApplication.class,
                args
        );
    }
}