package com.learners.orderservice.listener;

import com.learners.model.events.AllocationResult;
import com.learners.orderservice.config.JmsConfig;
import com.learners.orderservice.service.OrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationResultListener {

    private final OrderManager orderManager;

    @JmsListener(destination = JmsConfig.ALLOCATION_RESULT_QUEUE)
    public void listen(AllocationResult result) {
        log.info("Allocation result message arrived: {}", result);
        orderManager.processAllocationResult(result);
    }
}
