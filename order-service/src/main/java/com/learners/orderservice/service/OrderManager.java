package com.learners.orderservice.service;

import com.learners.model.events.AllocationResult;
import com.learners.orderservice.entity.Order;

import java.util.UUID;

public interface OrderManager {

    Order createOrder(Order order);
    void processValidationResult(UUID orderId, boolean valid);
    void processAllocationResult(AllocationResult result);
}
