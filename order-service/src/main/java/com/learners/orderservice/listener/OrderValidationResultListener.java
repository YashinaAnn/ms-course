package com.learners.orderservice.listener;

import com.learners.model.events.ValidationResultEvent;
import com.learners.orderservice.config.JmsConfig;
import com.learners.orderservice.service.OrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidationResultListener {

    private final OrderManager orderManager;

    @JmsListener(destination = JmsConfig.VALIDATION_RESULT_QUEUE)
    public void listen(ValidationResultEvent event) {
        log.info("Validation order result: {}", event);

        orderManager.processValidation(event.getOrderId(), event.isValid());
    }
}
