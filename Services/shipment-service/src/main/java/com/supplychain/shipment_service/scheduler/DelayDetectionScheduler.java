package com.supplychain.shipment_service.scheduler;

import com.supplychain.shipment_service.entity.Shipment;
import com.supplychain.shipment_service.enums.ShipmentStatus;
import com.supplychain.shipment_service.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DelayDetectionScheduler {

    private final ShipmentRepository shipmentRepository;

    @Scheduled(cron = "0 */15 * * * *")
    public void detectDelayedShipments() {

        log.info("Checking delayed shipments...");

        List<Shipment> delayedShipments =
                shipmentRepository.findDelayedShipments(
                        LocalDateTime.now()
                );

        for (Shipment shipment : delayedShipments) {

            shipment.setCurrentStatus(ShipmentStatus.DELAYED);

            shipmentRepository.save(shipment);

            log.info(
                    "Shipment marked delayed: {}",
                    shipment.getId()
            );
        }
    }
}