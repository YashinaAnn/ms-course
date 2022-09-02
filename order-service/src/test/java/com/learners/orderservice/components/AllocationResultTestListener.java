package com.learners.orderservice.components;

import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.events.AllocateOrderRequest;
import com.learners.model.events.AllocationResult;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.learners.orderservice.BaseTest.*;

@Profile("test")
@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationResultTestListener {

    private final JmsTemplate jmsTemplate;
    private final AppConfigs configs;

    @JmsListener(destination = "${app.config.allocate-order-queue}")
    public void listen(AllocateOrderRequest request) {
        OrderDto order = request.getOrder();

        if (skipAllocation(order)) {
            log.info("Skipping allocation...");
            return;
        }

        boolean pendingInventory = pendingInventory(order);
        if (!pendingInventory) {
            for (OrderLineDto orderLine : order.getOrderLines()) {
                orderLine.setQuantityAllocated(orderLine.getQuantityOrdered());
            }
        }

        jmsTemplate.convertAndSend(configs.getAllocationResultQueue(),
                AllocationResult.builder()
                        .order(order)
                        .pendingInventory(pendingInventory)
                        .exception(allocationException(order))
                        .build());
    }

    private boolean skipAllocation(OrderDto order) {
        return order.getOrderLines().stream()
                .anyMatch(orderLine -> NO_ALLOCATION.equals(orderLine.getPizzaName()));
    }

    private boolean pendingInventory(OrderDto order) {
        return order.getOrderLines().stream()
                .anyMatch(orderLine -> PENDING_INVENTORY.equals(orderLine.getPizzaName()));
    }

    private boolean allocationException(OrderDto order) {
        return order.getOrderLines().stream()
                .anyMatch(orderLine -> ALLOCATION_ERROR.equals(orderLine.getPizzaName()));
    }
}
