package com.learners.orderservice.service.impl;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.events.AllocationResult;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.repository.OrderRepository;
import com.learners.orderservice.service.OrderManager;
import com.learners.orderservice.sm.OrderStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderManagerImpl implements OrderManager {

    public static final String ORDER_ID_HEADER = "order_id";

    private final StateMachineFactory<OrderStatus, OrderEvent> machineFactory;
    private final OrderStateChangeInterceptor stateChangeInterceptor;
    private final OrderRepository repository;

    @Override
    public Order createOrder(Order order) {
        order.setOrderStatus(OrderStatus.NEW);
        order.setId(null);

        order = repository.save(order);
        sendEvent(order, OrderEvent.VALIDATE_ORDER);
        return order;
    }

    @Override
    public void processValidationResult(UUID orderId, boolean valid) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (valid) {
            sendEvent(order, OrderEvent.VALIDATION_SUCCESS);
            Order validatedOrder = repository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
            sendEvent(validatedOrder, OrderEvent.ALLOCATE_ORDER);
        } else {
            sendEvent(order, OrderEvent.VALIDATION_FAILED);
        }
    }

    @Override
    public void processAllocationResult(AllocationResult result) {
        if (!result.isException() && !result.isPendingInventory()) {
            processAllocationSuccess(result.getOrder());
        } else if (result.isException()) {
            processAllocationFailed(result.getOrder());
        } else {
            processAllocationPendingInventory(result.getOrder());
        }
    }

    private void processAllocationSuccess(OrderDto orderDto) {
        Order order = updateInventory(orderDto);
        sendEvent(order, OrderEvent.ALLOCATION_SUCCESS);
    }

    private void processAllocationPendingInventory(OrderDto orderDto) {
        Order order = updateInventory(orderDto);
        sendEvent(order, OrderEvent.ALLOCATION_NO_INVENTORY);
    }

    private void processAllocationFailed(OrderDto orderDto) {
        Order order = repository.findById(orderDto.getId())
                .orElseThrow(() -> new OrderNotFoundException(orderDto.getId()));
        sendEvent(order, OrderEvent.ALLOCATION_FAILED);
    }

    private Order updateInventory(OrderDto orderDto) {
        Order order = repository.findById(orderDto.getId())
                .orElseThrow(() -> new OrderNotFoundException(orderDto.getId()));
        order.getOrderLines().forEach(orderLine -> {
            orderDto.getOrderLines().forEach(orderLineDto -> {
                if (orderLine.getId().equals(orderLineDto.getId())) {
                    orderLine.setQtyAllocated(orderLineDto.getQuantityAllocated());
                }
            });
        });
        return repository.save(order);
    }

    private void sendEvent(Order order, OrderEvent event) {
        StateMachine<OrderStatus, OrderEvent> machine = build(order);
        Message<OrderEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getId())
                .build();
        machine.sendEvent(message);
    }

    private StateMachine<OrderStatus, OrderEvent> build(Order order) {
        StateMachine<OrderStatus, OrderEvent> machine = machineFactory.getStateMachine(order.getId().toString());
        machine.stop();
        machine.getStateMachineAccessor().doWithAllRegions(
                sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderStatus(), null, null, null));
                    sma.addStateMachineInterceptor(stateChangeInterceptor);
                }
        );
        machine.start();
        return machine;
    }
}