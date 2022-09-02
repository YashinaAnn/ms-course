package com.learners.pizzaservice.validator;

import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.pizzaservice.repository.PizzaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final PizzaRepository repository;

    public boolean validate(OrderDto order) {
        boolean valid = true;
        for (OrderLineDto orderLine : order.getOrderLines()) {
            if (repository.findById(orderLine.getPizzaId()).isEmpty()) {
                valid = false;
                break;
            }
        }
        return valid;
    }
}
