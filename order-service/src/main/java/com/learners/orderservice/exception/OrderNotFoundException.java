package com.learners.orderservice.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID id) {
        super(String.format("Order with id %s not found", id));
    }

    public OrderNotFoundException() {
        super();
    }
}
