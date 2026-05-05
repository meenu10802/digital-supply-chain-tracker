package com.supplychain.auth.messaging;

import com.supplychain.auth.config.RabbitMQConfig;
import com.supplychain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /*public void publishUserRegistered(User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        payload.put("email", user.getEmail());
        payload.put("fullName", user.getFullName());
        payload.put("roles", user.getRoles());
        payload.put("registeredAt", LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.USER_REGISTERED_KEY,
                payload);

        log.info("Published UserRegistered event for: {}",
                user.getEmail());
    }*/
    public void publishUserRegistered(User user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getId());
            payload.put("email", user.getEmail());
            payload.put("fullName", user.getFullName());
            payload.put("roles", user.getRoles());
            payload.put("registeredAt", LocalDateTime.now().toString());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.USER_REGISTERED_KEY,
                    payload);

            log.info("Published UserRegistered event for: {}",
                    user.getEmail());
        } catch (Exception e) {
            log.warn("RabbitMQ unavailable, skipping UserRegistered event for: {}. Error: {}",
                    user.getEmail(), e.getMessage());
        }
    }
}