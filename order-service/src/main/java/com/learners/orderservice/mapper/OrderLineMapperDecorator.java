package com.learners.orderservice.mapper;

import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.model.dto.OrderLineDto;
import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class OrderLineMapperDecorator implements OrderLineMapper {

    @Autowired
    @Qualifier("delegate")
    private OrderLineMapper mapper;

    @Autowired
    private PizzaService pizzaService;

    @Override
    public OrderLineDto orderLineToDto(OrderLine orderLine) {
        OrderLineDto dto = mapper.orderLineToDto(orderLine);

        PizzaDto pizza = pizzaService.getPizzaById(orderLine.getPizzaId());
        dto.setPizzaName(pizza.getName());
        dto.setType(pizza.getType());
        return dto;
    }
}