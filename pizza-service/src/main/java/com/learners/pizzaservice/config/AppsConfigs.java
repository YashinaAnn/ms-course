package com.learners.pizzaservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppsConfigs {

    private int defaultPage;
    private int defaultSize;
    private int minimalQuantity;
    private int quantityToDeliver;

    private String inventoryServiceHost;
    private String inventoryPath;
    private String inventoryUser;
    private String inventoryPassword;

    private String deliveryRequestQueue;
    private String newInventoryQueue;
    private String validateOrderQueue;
    private String validationResultQueue;
}
