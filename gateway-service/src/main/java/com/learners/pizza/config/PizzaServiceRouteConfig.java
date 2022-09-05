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
                        .uri("lb://pizza-service"))
                .route(r -> r.path("/api/v1/inventory/*")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("inventoryServiceCB")
                                .setFallbackUri("forward:/api/v1/inventory-failover")))
                        .uri("lb://inventory-service"))
                .route(r -> r.path("/api/v1/customers**")
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/v1/inventory-failover")
                        .uri("lb://inventory-failover-service"))
                .build();
    }
}
