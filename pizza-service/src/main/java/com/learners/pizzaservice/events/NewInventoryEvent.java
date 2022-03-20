package com.learners.pizzaservice.events;

import com.learners.pizzaservice.model.PizzaDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends PizzaEvent {

    public NewInventoryEvent(PizzaDto pizzaDto) {
        super(pizzaDto);
    }
}
