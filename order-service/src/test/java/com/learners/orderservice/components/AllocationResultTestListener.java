package com.learners.orderservice.components;

import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.events.AllocateOrderRequest;
import com.learners.model.events.AllocationResult;
import com.learners.orderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.learners.orderservice.BaseTest.ALLOCATION_ERROR_PIZZA;
import static com.learners.orderservice.BaseTest.PENDING_INVENTORY_PIZZA;

@Profile("test")
@Component
@RequiredArgsConstructor
public class AllocationResultTestListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        OrderDto order = request.getOrder();

        boolean pendingInventory = order.getOrderLines().stream()
                .anyMatch(orderLine -> PENDING_INVENTORY_PIZZA.equals(orderLine.getPizzaName()));
        boolean exception = order.getOrderLines().stream()
                .anyMatch(orderLine -> ALLOCATION_ERROR_PIZZA.equals(orderLine.getPizzaName()));

        if (!pendingInventory) {
            for (OrderLineDto orderLine : order.getOrderLines()) {
                orderLine.setQuantityAllocated(orderLine.getQuantityOrdered());
            }
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATION_RESULT_QUEUE,
                AllocationResult.builder()
                        .order(order)
                        .pendingInventory(pendingInventory)
                        .exception(exception)
                        .build());
    }
}
