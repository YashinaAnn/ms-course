package com.learners.pizzaservice.mapper;

import com.learners.pizzaservice.entity.Pizza;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PizzaMapperDecorator extends PizzaMapper {

    @Autowired
    @Qualifier("delegate")
    private PizzaMapper pizzaMapper;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public PizzaDto pizzaToDto(Pizza pizza, boolean inventoryEnabled) {
        PizzaDto dto = pizzaMapper.pizzaToDto(pizza, inventoryEnabled);
        if (inventoryEnabled) {
            dto.setInventoryOnHand(inventoryService.getInventoryByPizzaId(pizza.getId()));
        }
        return dto;
    }
}