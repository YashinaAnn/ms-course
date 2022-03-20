package com.learners.pizzaservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final AppsConfigs configs;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) { // TODO
        return restTemplateBuilder
                .rootUri(configs.getInventoryServiceHost())
                .build();
    }
}
