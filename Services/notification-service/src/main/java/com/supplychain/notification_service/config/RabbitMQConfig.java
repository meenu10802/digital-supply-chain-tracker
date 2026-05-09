package com.supplychain.notification_service.config;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // =========================================================
    // EXCHANGES
    // =========================================================

    public static final String SUPPLY_EXCHANGE =
            "supply.exchange";

    public static final String DEAD_LETTER_EXCHANGE =
            "supply.dlx";

    // =========================================================
    // QUEUES
    // =========================================================

    public static final String USER_QUEUE =
            "user.notification.queue";

    public static final String SHIPMENT_QUEUE =
            "shipment.notification.queue";

    public static final String STATUS_QUEUE =
            "status.notification.queue";

    public static final String ALERT_QUEUE =
            "alert.notification.queue";

    public static final String DEAD_LETTER_QUEUE =
            "notification.dlq";

    // =========================================================
    // ROUTING KEYS
    // =========================================================

    public static final String USER_REGISTERED_KEY =
            "user.registered";

    public static final String SHIPMENT_CREATED_KEY =
            "shipment.created";

    public static final String SHIPMENT_STATUS_CHANGED_KEY =
            "shipment.status.changed";

    public static final String ALERT_TRIGGERED_KEY =
            "alert.triggered";

    public static final String DLQ_ROUTING_KEY =
            "notification.failed";

    // =========================================================
    // MAIN EXCHANGE
    // =========================================================

    @Bean
    public TopicExchange supplyExchange() {

        return new TopicExchange(
                SUPPLY_EXCHANGE
        );
    }

    // =========================================================
    // DLX
    // =========================================================

    @Bean
    public TopicExchange deadLetterExchange() {

        return new TopicExchange(
                DEAD_LETTER_EXCHANGE
        );
    }

    // =========================================================
    // USER QUEUE
    // =========================================================

    @Bean
    public Queue userQueue() {

        return QueueBuilder
                .durable(USER_QUEUE)

                .withArgument(
                        "x-dead-letter-exchange",
                        DEAD_LETTER_EXCHANGE
                )

                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )

                .build();
    }

    // =========================================================
    // SHIPMENT QUEUE
    // =========================================================

    @Bean
    public Queue shipmentQueue() {

        return QueueBuilder
                .durable(SHIPMENT_QUEUE)

                .withArgument(
                        "x-dead-letter-exchange",
                        DEAD_LETTER_EXCHANGE
                )

                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )

                .build();
    }

    // =========================================================
    // STATUS QUEUE
    // =========================================================

    @Bean
    public Queue statusQueue() {

        return QueueBuilder
                .durable(STATUS_QUEUE)

                .withArgument(
                        "x-dead-letter-exchange",
                        DEAD_LETTER_EXCHANGE
                )

                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )

                .build();
    }

    // =========================================================
    // ALERT QUEUE
    // =========================================================

    @Bean
    public Queue alertQueue() {

        return QueueBuilder
                .durable(ALERT_QUEUE)

                .withArgument(
                        "x-dead-letter-exchange",
                        DEAD_LETTER_EXCHANGE
                )

                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )

                .build();
    }

    // =========================================================
    // DLQ
    // =========================================================

    @Bean
    public Queue deadLetterQueue() {

        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    // =========================================================
    // BINDINGS
    // =========================================================

    @Bean
    public Binding userBinding() {

        return BindingBuilder
                .bind(userQueue())
                .to(supplyExchange())
                .with(USER_REGISTERED_KEY);
    }

    @Bean
    public Binding shipmentBinding() {

        return BindingBuilder
                .bind(shipmentQueue())
                .to(supplyExchange())
                .with(SHIPMENT_CREATED_KEY);
    }

    @Bean
    public Binding statusBinding() {

        return BindingBuilder
                .bind(statusQueue())
                .to(supplyExchange())
                .with(SHIPMENT_STATUS_CHANGED_KEY);
    }

    @Bean
    public Binding alertBinding() {

        return BindingBuilder
                .bind(alertQueue())
                .to(supplyExchange())
                .with(ALERT_TRIGGERED_KEY);
    }

    @Bean
    public Binding dlqBinding() {

        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}