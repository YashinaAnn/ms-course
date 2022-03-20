package com.learners.pizzaservice.events;

import com.learners.pizzaservice.model.PizzaDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PizzaDeliveryEvent extends PizzaEvent {

    public PizzaDeliveryEvent(PizzaDto pizzaDto) {
        super(pizzaDto);
    }
}
