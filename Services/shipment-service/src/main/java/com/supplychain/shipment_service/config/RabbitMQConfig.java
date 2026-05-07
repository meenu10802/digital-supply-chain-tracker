package com.supplychain.shipment_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange supplyExchange() {

        return new TopicExchange("supply.exchange");
    }
}