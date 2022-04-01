package com.learners.orderservice.service;

import com.learners.model.dto.order.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface OrderService {

    Page<OrderDto> getOrders(UUID customerId, PageRequest pageRequest);
    OrderDto getOrderById(UUID customerId, UUID orderId);
    OrderDto placeOrder(UUID customerId, OrderDto orderDto);
    void pickUpOrder(UUID customerId, UUID orderId);
}
