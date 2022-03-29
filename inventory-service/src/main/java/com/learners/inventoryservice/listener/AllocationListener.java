package com.learners.inventoryservice.listener;

import com.learners.inventoryservice.config.JmsConfig;
import com.learners.inventoryservice.service.AllocationService;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.events.AllocateOrderRequest;
import com.learners.model.events.AllocationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        log.info("Allocation order request is arrived: {}", request);

        AllocationResult result = allocateOrder(request.getOrder());
        log.info("Allocation order result: {}", result);
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATION_RESULT_QUEUE, result);
    }

    private AllocationResult allocateOrder(OrderDto order) {
        AllocationResult.AllocationResultBuilder builder = AllocationResult.builder();
        try {
            boolean allocated = allocationService.allocateOrder(order);
            builder.exception(false);
            builder.pendingInventory(!allocated);
        } catch (Exception e) {
            log.error("Exception occured during order allocation: {}", e.getStackTrace());
            builder.exception(true);
        }
        builder.order(order);
        return builder.build();
    }
}
