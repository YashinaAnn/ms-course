package com.learners.pizzaservice.client;

import com.learners.model.dto.inventory.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("inventory-failover-service")
public interface InventoryFailoverClient {

    @GetMapping("/api/v1/inventory-failover")
    ResponseEntity<List<InventoryDto>> getDefaultInventory();
}
