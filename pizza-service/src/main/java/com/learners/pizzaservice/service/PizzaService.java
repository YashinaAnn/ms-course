package com.learners.pizzaservice.service;

import com.learners.model.dto.PizzaDto;
import com.learners.model.dto.PizzaListDto;
import org.springframework.data.domain.PageRequest;

public interface PizzaService {

    PizzaListDto getAll(PageRequest pageRequest, boolean inventoryEnabled);
    PizzaDto getPizzaById(long id, boolean inventoryEnabled);
    PizzaDto getPizzaByUpc(String upc, boolean inventoryEnabled);
    PizzaDto createPizza(PizzaDto dto);
    PizzaDto updatePizza(long id, PizzaDto dto);
    void deletePizza(long id);
}
