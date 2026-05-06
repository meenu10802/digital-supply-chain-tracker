package com.supplychain.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supply Chain — User Service")
                        .version("1.0")
                        .description("User profile management microservice"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public OperationCustomizer addGatewayHeaders() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(
                    new HeaderParameter()
                            .name("X-User-Id")
                            .description("Simulated gateway header — enter any number e.g. 1")
                            .required(false)
                            .schema(new StringSchema()._default("1"))
            );
            operation.addParametersItem(
                    new HeaderParameter()
                            .name("X-User-Roles")
                            .description("Simulated gateway header — ADMIN / SUPPLIER / TRANSPORTER / WAREHOUSE_MANAGER")
                            .required(false)
                            .schema(new StringSchema()._default("ADMIN"))
            );
            operation.addParametersItem(
                    new HeaderParameter()
                            .name("X-User-Email")
                            .description("Simulated gateway header — enter any email")
                            .required(false)
                            .schema(new StringSchema()._default("admin@test.com"))
            );
            return operation;
        };
    }
}