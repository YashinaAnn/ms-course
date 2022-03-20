package com.learners.orderservice.service.impl;

import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.service.PizzaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final RestTemplate restTemplate;
    private final AppConfigs configs;

    @Override
    public PizzaDto getPizzaById(Long id) {
        try {
            ResponseEntity<PizzaDto> response = restTemplate.getForEntity(
                    configs.getPizzaServiceHost() + configs.getPizzaServicePath() + id,
                    PizzaDto.class);
            return response.getBody();
        } catch (RestClientResponseException e) {
            log.error("Not able to get pizza details, {}", e.getMessage());
             // TODO - add handling
            return PizzaDto.builder().build();
        }
    }
}
