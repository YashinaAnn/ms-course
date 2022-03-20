package com.learners.inventoryservice.integration;

import com.learners.inventoryservice.controller.InventoryController;
import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.exception.PizzaNotFoundException;
import com.learners.inventoryservice.model.dto.InventoryDto;
import com.learners.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"test"})
@SpringBootTest
public class InventoryIntegrationTest {

    @Autowired
    private InventoryController controller;
    @Autowired
    private InventoryRepository repository;

    @Test
    public void testGetAllByPizzaId() {
        long pizzaId = 123L;
        repository.save(getValidInventory(pizzaId));
        repository.save(getValidInventory(pizzaId));
        ResponseEntity<List<InventoryDto>> response = controller.getInventoryByPizzaId(pizzaId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void testGetAllByNonExistingPizzaId() {
        assertThrows(PizzaNotFoundException.class, () -> controller.getInventoryByPizzaId(1234L));
    }

    private Inventory getValidInventory(long pizzaId) {
        return Inventory.builder()
                .pizzaId(pizzaId)
                .inventoryOnHand(100)
                .build();
    }
}
