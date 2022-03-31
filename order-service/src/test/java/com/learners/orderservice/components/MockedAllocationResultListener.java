package com.learners.orderservice.components;

import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.events.AllocateOrderRequest;
import com.learners.model.events.AllocationResult;
import com.learners.orderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
@RequiredArgsConstructor
public class MockedAllocationResultListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        for (OrderLineDto orderLine : request.getOrder().getOrderLines()) {
            orderLine.setQuantityAllocated(orderLine.getQuantityOrdered());
        }
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATION_RESULT_QUEUE,
                AllocationResult.builder()
                        .order(request.getOrder())
                        .pendingInventory(false)
                        .exception(false)
                        .build());
    }
}
