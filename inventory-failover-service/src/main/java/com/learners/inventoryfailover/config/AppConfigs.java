package com.learners.inventoryfailover.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.config")
@Data
public class AppConfigs {

    private int defaultInventory;
}
