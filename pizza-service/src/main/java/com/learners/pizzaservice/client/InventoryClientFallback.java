package com.learners.pizzaservice.client;

import com.learners.model.dto.inventory.InventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryClientFallback implements InventoryClient {

    private final InventoryFailoverClient inventoryFailoverClient;

    @Override
    public ResponseEntity<List<InventoryDto>> getInventoryByPizzaId(long id) {
        log.info("InventoryClientFallback:: getting default inventory");
        return inventoryFailoverClient.getDefaultInventory();
    }
}
