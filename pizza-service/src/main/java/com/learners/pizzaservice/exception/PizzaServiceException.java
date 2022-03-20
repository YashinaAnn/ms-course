package com.learners.pizzaservice.exception;

public class PizzaServiceException extends RuntimeException {

    public PizzaServiceException(String message) {
        super(message);
    }

    public PizzaServiceException() {
        super();
    }
}
