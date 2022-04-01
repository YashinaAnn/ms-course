package com.learners.orderservice.service.impl;

import com.learners.model.dto.order.OrderDto;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.mapper.OrderMapper;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.repository.OrderRepository;
import com.learners.orderservice.service.OrderManager;
import com.learners.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final OrderManager orderManager;

    @Override
    public Page<OrderDto> getOrders(UUID customerId, PageRequest pageRequest) {
        validateCustomerExists(customerId);
        return orderRepository.findByCustomerId(customerId, pageRequest)
                .map(orderMapper::orderToDto);
    }

    @Override
    public OrderDto getOrderById(UUID customerId, UUID orderId) {
        validateCustomerExists(customerId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        validateOrderBelongsToCustomer(customerId, order);

        return orderMapper.orderToDto(order);
    }

    @Override
    public OrderDto placeOrder(UUID customerId, OrderDto orderDto) {
        orderDto.setCustomerId(customerId);
        Order order = orderManager.createOrder(orderMapper.dtoToOrder(orderDto));
        log.info("Saved order: {}", order);
        return orderMapper.orderToDto(order);
    }

    @Override
    public void pickUpOrder(UUID customerId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        validateOrderBelongsToCustomer(customerId, order);
        orderManager.pickUp(orderId);
    }

    private Customer validateCustomerExists(UUID customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private void validateOrderBelongsToCustomer(UUID customerId, Order order) {
        if (!Objects.requireNonNull(customerId).equals(order.getCustomer().getId())) {
            throw new OrderNotFoundException(order.getId());
        }
    }
}
