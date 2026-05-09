// service/strategy/DamageAlertHandler.java
package com.example.alert.service.strategy;

import com.example.alert.entity.Alert;
import com.example.alert.enums.AlertSeverity;
import com.example.alert.enums.AlertType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DamageAlertHandler implements AlertHandler {

    @Override
    public Alert handle(Map<String, Object> eventPayload, String shipmentReference) {
        Long shipmentId = Long.valueOf(eventPayload.get("shipmentId").toString());

        String notes = eventPayload.containsKey("notes")
                ? eventPayload.get("notes").toString() : "No additional details provided";

        String message = String.format(
                "Shipment [%s] has been reported as DAMAGED. Notes: %s",
                shipmentReference != null ? shipmentReference : shipmentId,
                notes
        );

        return Alert.builder()
                .shipmentId(shipmentId)
                .shipmentReference(shipmentReference)
                .alertType(AlertType.DAMAGE)
                .message(message)
                .severity(AlertSeverity.CRITICAL)
                .isResolved(false)
                .build();
    }
}