package com.supplychain.notification_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()

                                .title(
                                        "Supply Chain Notification Service"
                                )

                                .description(
                                        "Microservice for handling email notifications using RabbitMQ"
                                )

                                .version(
                                        "1.0"
                                )

                                .contact(
                                        new Contact()
                                                .name("Kavish Dhiman")
                                                .email("testfor4work@gmail.com")
                                )

                                .license(
                                        new License()
                                                .name("Open Source")
                                )
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description(
                                        "Project Documentation"
                                )
                );
    }
}