package com.learners.pizza.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PizzaServiceRouteConfig {

    @Bean
    public RouteLocator pizzaServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/pizza**")
                        .uri("http://localhost:8080"))
                .route(r -> r.path("/api/v1/inventory/*")
                        .uri("http://localhost:8082"))
                .route(r -> r.path("/api/v1/customers**")
                        .uri("http://localhost:8081"))
                .build();
    }
}
