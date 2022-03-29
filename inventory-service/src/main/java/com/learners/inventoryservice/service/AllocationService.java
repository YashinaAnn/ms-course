package com.learners.inventoryservice.service;

import com.learners.model.dto.order.OrderDto;

public interface AllocationService {

    boolean allocateOrder(OrderDto order);
}
