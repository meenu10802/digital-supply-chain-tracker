// config/RabbitMQConfig.java
package com.example.alert.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange
    public static final String SUPPLY_EXCHANGE = "supply.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "supply.dlx";

    // Queues
    public static final String ALERT_QUEUE = "alert.queue";
    public static final String ALERT_DLQ   = "alert.dlq";

    // Routing keys this service CONSUMES
    public static final String ROUTING_DELAY  = "shipment.delay.detected";
    public static final String ROUTING_DAMAGE = "shipment.damage.reported";

    // Routing key this service PUBLISHES
    public static final String ROUTING_ALERT_TRIGGERED = "alert.triggered";

    @Bean
    public TopicExchange supplyExchange() {
        return new TopicExchange(SUPPLY_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue alertQueue() {
        return QueueBuilder.durable(ALERT_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ALERT_DLQ)
                .build();
    }

    @Bean
    public Queue alertDeadLetterQueue() {
        return QueueBuilder.durable(ALERT_DLQ).build();
    }

    // Bind alert.queue to supply.exchange for BOTH routing keys
    @Bean
    public Binding alertDelayBinding(Queue alertQueue, TopicExchange supplyExchange) {
        return BindingBuilder.bind(alertQueue)
                .to(supplyExchange)
                .with(ROUTING_DELAY);
    }

    @Bean
    public Binding alertDamageBinding(Queue alertQueue, TopicExchange supplyExchange) {
        return BindingBuilder.bind(alertQueue)
                .to(supplyExchange)
                .with(ROUTING_DAMAGE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}