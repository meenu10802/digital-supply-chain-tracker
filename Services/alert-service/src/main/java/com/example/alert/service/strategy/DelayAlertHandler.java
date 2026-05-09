// service/strategy/DelayAlertHandler.java
package com.example.alert.service.strategy;

import com.example.alert.entity.Alert;
import com.example.alert.enums.AlertSeverity;
import com.example.alert.enums.AlertType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DelayAlertHandler implements AlertHandler {

    @Override
    public Alert handle(Map<String, Object> eventPayload, String shipmentReference) {
        Long shipmentId = Long.valueOf(eventPayload.get("shipmentId").toString());

        String message = String.format(
                "Shipment [%s] has exceeded its expected delivery date and is now marked DELAYED.",
                shipmentReference != null ? shipmentReference : shipmentId
        );

        return Alert.builder()
                .shipmentId(shipmentId)
                .shipmentReference(shipmentReference)
                .alertType(AlertType.DELAY)
                .message(message)
                .severity(AlertSeverity.HIGH)
                .isResolved(false)
                .build();
    }
}