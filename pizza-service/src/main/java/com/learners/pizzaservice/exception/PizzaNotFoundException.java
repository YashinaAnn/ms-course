package com.learners.pizzaservice.exception;

public class PizzaNotFoundException extends PizzaServiceException {

    public PizzaNotFoundException(Long id) {
        super(String.format("Pizza with id %s not found", id));
    }

    public PizzaNotFoundException(String upc) {
        super(String.format("Pizza with upc %s not found", upc));
    }

    public PizzaNotFoundException() {
        super();
    }
}
