package com.learners.inventoryservice.exception;

public class PizzaNotFoundException extends RuntimeException {

    public PizzaNotFoundException(Long id) {
        super(String.format("Pizza with id %s not found", id));
    }

    public PizzaNotFoundException() {
        super();
    }
}
