package com.learners.inventoryfailover.controller;

import com.learners.inventoryfailover.service.InventoryFailoverService;
import com.learners.model.dto.order.InventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory-failover")
public class InventoryFailoverController {

    private final InventoryFailoverService service;

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getDefaultInventory() {
        return ResponseEntity.ok(service.getDefaultInventory());
    }
}
