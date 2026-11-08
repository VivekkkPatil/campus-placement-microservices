package com.vivek.placement.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    // lb:// means "look the service up in Eureka and load balance"
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("student-service", r -> r.path("/students/**").uri("lb://student-service"))
                .route("drive-service", r -> r.path("/drives/**", "/companies/**").uri("lb://drive-service"))
                .route("application-service", r -> r.path("/applications/**").uri("lb://application-service"))
                .build();
    }
}
