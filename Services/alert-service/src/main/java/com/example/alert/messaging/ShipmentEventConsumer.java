// messaging/ShipmentEventConsumer.java
package com.example.alert.messaging;

import com.example.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    private final AlertService alertService;

    @RabbitListener(queues = "alert.queue")
    public void handleShipmentEvent(
            Map<String, Object> payload,
            @Header("amqp_receivedRoutingKey") String routingKey) {

        log.debug("Received event — routingKey={} payload={}", routingKey, payload);

        try {
            switch (routingKey) {
                case "shipment.delay.detected"  -> alertService.processDelayEvent(payload);
                case "shipment.damage.reported" -> alertService.processDamageEvent(payload);
                default -> log.warn("Unrecognised routing key on alert.queue: {}", routingKey);
            }
        } catch (Exception e) {
            log.error("Failed to process event [{}] — will route to DLQ. Error: {}",
                    routingKey, e.getMessage(), e);
            throw e; // rethrow triggers DLQ after 3 retries
        }
    }
}