package com.learners.pizzaservice.exception;

import org.springframework.web.client.RestClientException;

public class InventoryServiceException extends RuntimeException {

    public InventoryServiceException(RestClientException e) {
        super(e);
    }
}
