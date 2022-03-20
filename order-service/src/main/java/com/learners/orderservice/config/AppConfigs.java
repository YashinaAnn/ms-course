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
}
