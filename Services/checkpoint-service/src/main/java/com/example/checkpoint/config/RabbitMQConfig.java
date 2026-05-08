// config/RabbitMQConfig.java
package com.example.checkpoint.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE    = "supply.exchange";
    public static final String QUEUE       = "checkpoint.queue";
    public static final String ROUTING_KEY = "shipment.status.changed";
    public static final String DLX         = "supply.dlx";
    public static final String DLQ         = "checkpoint.dlq";

    @Bean
    public TopicExchange supplyExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue checkpointQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .build();
    }

    @Bean
    public Binding checkpointBinding(Queue checkpointQueue, TopicExchange supplyExchange) {
        return BindingBuilder.bind(checkpointQueue)
                .to(supplyExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(messageConverter());
        return template;
    }
}