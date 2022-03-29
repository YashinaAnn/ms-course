package com.learners.orderservice.service.impl;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
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
        StateMachine<OrderStatus, OrderEvent> machine = build(order);
        sendEvent(machine, order.getId(), OrderEvent.VALIDATE_ORDER);
        return order;
    }

    @Override
    public void processValidation(UUID orderId, boolean valid) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        StateMachine<OrderStatus, OrderEvent> machine = build(order);
        OrderEvent event = valid ? OrderEvent.VALIDATION_SUCCESS : OrderEvent.VALIDATION_FAILED;

        sendEvent(machine, orderId, event);
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

    private void sendEvent(StateMachine<OrderStatus, OrderEvent> machine, UUID orderId, OrderEvent event) {
        Message<OrderEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(ORDER_ID_HEADER, orderId)
                .build();

        machine.sendEvent(message);
    }
}
