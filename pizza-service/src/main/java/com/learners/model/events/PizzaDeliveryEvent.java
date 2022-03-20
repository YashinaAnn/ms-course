package com.learners.model.events;

import com.learners.model.dto.PizzaDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PizzaDeliveryEvent extends PizzaEvent {

    public PizzaDeliveryEvent(PizzaDto pizzaDto) {
        super(pizzaDto);
    }
}
