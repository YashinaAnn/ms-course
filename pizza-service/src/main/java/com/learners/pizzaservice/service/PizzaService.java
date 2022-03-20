package com.learners.pizzaservice.service;

import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaListDto;
import org.springframework.data.domain.PageRequest;

public interface PizzaService {

    PizzaListDto getAll(PageRequest pageRequest, boolean inventoryEnabled);
    PizzaDto getPizzaById(long id, boolean inventoryEnabled);
    PizzaDto getPizzaByUpc(String upc, boolean inventoryEnabled);
    PizzaDto createPizza(PizzaDto dto);
    PizzaDto updatePizza(long id, PizzaDto dto);
    void deletePizza(long id);
}
