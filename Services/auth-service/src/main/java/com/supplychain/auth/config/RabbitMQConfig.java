package com.supplychain.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "supply.exchange";
    public static final String USER_REGISTERED_KEY = "user.registered";

    @Bean
    public TopicExchange supplyExchange() {
        return new TopicExchange(EXCHANGE);
    }
}