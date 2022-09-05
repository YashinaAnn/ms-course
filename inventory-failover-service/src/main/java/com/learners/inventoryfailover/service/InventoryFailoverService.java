package com.learners.inventoryfailover.service;

import com.learners.model.dto.order.InventoryDto;

import java.util.List;

public interface InventoryFailoverService {

    List<InventoryDto> getDefaultInventory();
}
