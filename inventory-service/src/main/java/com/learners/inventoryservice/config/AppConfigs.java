package com.learners.inventoryservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.config")
@Data
@NoArgsConstructor
public class AppConfigs {

    private String inventoryQueue;
    private String allocateOrderQueue;
    private String allocationResultQueue;
    private String deallocateOrderQueue;
}
