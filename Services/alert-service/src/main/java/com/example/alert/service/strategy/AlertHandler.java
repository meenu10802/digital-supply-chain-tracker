// service/strategy/AlertHandler.java
package com.example.alert.service.strategy;

import com.example.alert.entity.Alert;

import java.util.Map;

public interface AlertHandler {
    Alert handle(Map<String, Object> eventPayload, String shipmentReference);
}