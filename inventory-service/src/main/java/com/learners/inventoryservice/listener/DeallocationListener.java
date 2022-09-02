package com.learners.inventoryservice.listener;

import com.learners.inventoryservice.config.JmsConfig;
import com.learners.inventoryservice.service.AllocationService;
import com.learners.model.events.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocationListener {

    private final AllocationService allocationService;

    @JmsListener(destination = "${app.config.deallocate-order-queue}")
    public void listen(DeallocateOrderRequest request) {
        log.info("Deallocate order request arrived: {}", request);
        allocationService.deallocateOrder(request.getOrder());
    }
}
