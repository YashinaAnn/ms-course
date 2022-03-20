package com.learners.pizzaservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String DELIVERY_REQUEST_QUEUE = "delivery-request";
    public static final String NEW_INVENTORY_QUEUE = "inventory";

    @Bean
    public MessageConverter messageConverter(ObjectMapper mapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setTypeIdPropertyName("_type");
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }
}
