// config/FeignClientConfig.java
package com.example.alert.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor forwardHeadersInterceptor() {
        return template -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                var req = attrs.getRequest();
                String userId = req.getHeader("X-User-Id");
                String roles  = req.getHeader("X-User-Roles");
                if (userId != null) template.header("X-User-Id", userId);
                if (roles  != null) template.header("X-User-Roles", roles);
            }
        };
    }
}