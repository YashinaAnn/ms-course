package com.learners.pizzaservice.service;

import java.util.Optional;

public interface InventoryService {

    Optional<Integer> getInventoryByPizzaId(long pizzaId);
}
