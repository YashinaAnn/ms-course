package com.learners.orderservice.service;

import com.learners.orderservice.entity.Order;

import java.util.UUID;

public interface OrderManager {

    Order createOrder(Order order);
    void processValidation(UUID orderId, boolean valid);
}
