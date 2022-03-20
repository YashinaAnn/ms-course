package com.learners.pizzaservice.service.impl;

import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.model.InventoryDto;
import com.learners.pizzaservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final RestTemplate restTemplate;
    private final AppsConfigs configs;

    @Override
    public Integer getInventoryByPizzaId(long pizzaId) {
        try {
            ResponseEntity<List<InventoryDto>> response = restTemplate.exchange(
                     configs.getInventoryPath() + pizzaId,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<InventoryDto>>(){});

            return Objects.requireNonNull(response.getBody()).stream()
                    .map(InventoryDto::getInventoryOnHand)
                    .reduce(0, Integer::sum);

        } catch (RestClientException e) {
            log.error("Error while getting inventory for product with id {}. Inventory service error: {}", pizzaId, e.getMessage());
        }
        return null;
    }
}