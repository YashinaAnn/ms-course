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

    private String inventoryServiceHost;
    private String inventoryPath;
}
