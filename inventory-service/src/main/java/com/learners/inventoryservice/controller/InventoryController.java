package com.learners.inventoryservice.controller;

import com.learners.inventoryservice.model.dto.InventoryDto;
import com.learners.inventoryservice.service.InventoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Getter
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{pizzaId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByPizzaId(@PathVariable("pizzaId") Long pizzaId) {
        return ResponseEntity.ok(service.getByPizzaId(pizzaId));
    }
}
