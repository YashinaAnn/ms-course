package com.learners.pizzaservice.service.impl;

import com.learners.pizzaservice.client.InventoryClient;
import com.learners.model.dto.inventory.InventoryDto;
import com.learners.pizzaservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryClient inventoryClient;

    @Override
    public Optional<Integer> getInventoryByPizzaId(long pizzaId) {
        try {
            log.debug("Calling inventory service for pizza id {}", pizzaId);
            ResponseEntity<List<InventoryDto>> response = inventoryClient.getInventoryByPizzaId(pizzaId);

            Integer inventory = Objects.requireNonNull(response.getBody()).stream()
                    .map(InventoryDto::getInventoryOnHand)
                    .reduce(0, Integer::sum);
            log.debug("Inventory for pizzaId {} is {}", pizzaId, inventory);
            return Optional.of(inventory);

        } catch (RestClientException e) {
            log.error("Error while getting inventory for product with id {}. Inventory service error: ", pizzaId, e);
        }
        return Optional.empty();
    }
}