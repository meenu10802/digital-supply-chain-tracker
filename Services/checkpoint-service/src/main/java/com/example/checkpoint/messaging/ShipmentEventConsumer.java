// messaging/ShipmentEventConsumer.java
package com.example.checkpoint.messaging;

import com.example.checkpoint.service.CheckpointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    private final CheckpointService checkpointService;

    @RabbitListener(queues = "checkpoint.queue")
    public void handleShipmentStatusChanged(Map<String, Object> message) {
        try {
            Long shipmentId = Long.valueOf(message.get("shipmentId").toString());
            String status   = message.get("newStatus").toString();
            String location = message.containsKey("location")
                    ? message.get("location").toString() : null;
            Long userId     = message.containsKey("updatedByUserId")
                    ? Long.valueOf(message.get("updatedByUserId").toString()) : null;

            log.info("Received ShipmentStatusChanged event — shipmentId={} status={}", shipmentId, status);
            checkpointService.autoLogFromEvent(shipmentId, status, location, userId);

        } catch (Exception e) {
            log.error("Failed to process ShipmentStatusChanged event: {}", message, e);
            throw e; // rethrow so RabbitMQ triggers DLQ retry
        }
    }
}