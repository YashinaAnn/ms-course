package com.learners.pizzaservice.client;

import com.learners.model.dto.inventory.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("inventory-service")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory/{id}")
    ResponseEntity<List<InventoryDto>> getInventoryByPizzaId(@PathVariable long id);
}
