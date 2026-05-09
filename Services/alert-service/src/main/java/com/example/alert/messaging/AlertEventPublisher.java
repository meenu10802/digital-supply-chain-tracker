// messaging/AlertEventPublisher.java
package com.example.alert.messaging;

import com.example.alert.config.RabbitMQConfig;
import com.example.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAlertTriggered(Alert alert) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("alertId",            alert.getId());
        payload.put("shipmentId",         alert.getShipmentId());
        payload.put("shipmentReference",  alert.getShipmentReference());
        payload.put("alertType",          alert.getAlertType().name());
        payload.put("severity",           alert.getSeverity().name());
        payload.put("message",            alert.getMessage());
        payload.put("triggeredAt",        LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SUPPLY_EXCHANGE,
                RabbitMQConfig.ROUTING_ALERT_TRIGGERED,
                payload
        );

        log.info("Published AlertTriggered — alertId={} type={} shipmentId={}",
                alert.getId(), alert.getAlertType(), alert.getShipmentId());
    }
}