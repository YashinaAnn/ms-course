package com.learners.orderservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfigs {

    private int defaultPage;
    private int defaultSize;

    private String pizzaServiceHost;
    private String pizzaServicePath;

    private String validateOrderQueue;
    private String validationResultQueue;
    private String allocateOrderQueue;
    private String allocationResultQueue;
    private String deallocateOrderQueue;
    private String allocationErrorQueue;

}
