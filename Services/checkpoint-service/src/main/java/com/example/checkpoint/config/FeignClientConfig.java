// config/FeignClientConfig.java
package com.example.checkpoint.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                var request = attrs.getRequest();
                String userId = request.getHeader("X-User-Id");
                String roles  = request.getHeader("X-User-Roles");
                if (userId != null) requestTemplate.header("X-User-Id", userId);
                if (roles  != null) requestTemplate.header("X-User-Roles", roles);
            }
        };
    }
}