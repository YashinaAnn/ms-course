package com.learners.pizzaservice.mapper;

import com.learners.pizzaservice.entity.Pizza;
import com.learners.model.dto.PizzaDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
@DecoratedWith(PizzaMapperDecorator.class)
public abstract class PizzaMapper {

    public abstract PizzaDto pizzaToDto(Pizza pizza);
    public abstract PizzaDto pizzaToDto(Pizza pizza, boolean inventoryEnabled);
    public abstract Pizza dtoToPizza(PizzaDto dto);
    public abstract void updatePizzaFromDto(PizzaDto dto, @MappingTarget Pizza pizza);
}
