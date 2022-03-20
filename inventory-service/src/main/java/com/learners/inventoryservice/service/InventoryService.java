package com.learners.inventoryservice.service;

import com.learners.model.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getByPizzaId(Long id);
}
