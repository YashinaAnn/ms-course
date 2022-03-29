package com.learners.orderservice.service.impl;

import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.mapper.OrderMapper;
import com.learners.model.OrderStatus;
import com.learners.model.dto.OrderDto;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.repository.OrderLineRepository;
import com.learners.orderservice.repository.OrderRepository;
import com.learners.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

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

        if (customerId.equals(order.getCustomer().getId())) {
            return orderMapper.orderToDto(order);
        }
        throw new OrderNotFoundException(orderId);
    }

    @Override
    public OrderDto placeOrder(UUID customerId, OrderDto orderDto) {
        orderDto.setCustomerId(customerId);
        Order order = orderMapper.dtoToOrder(orderDto);
        order.setOrderStatus(OrderStatus.NEW);

        for (OrderLine orderLine: order.getOrderLines()) {
            orderLine.setOrder(order);
        }

        return orderMapper.orderToDto(orderRepository.save(order));
    }

    private Customer validateCustomerExists(UUID customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
