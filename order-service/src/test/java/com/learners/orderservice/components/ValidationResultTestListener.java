package com.learners.orderservice.components;

import com.learners.model.events.ValidateOrderRequest;
import com.learners.model.events.ValidationResult;
import com.learners.orderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("test")
@Component
@RequiredArgsConstructor
public class ValidationResultTestListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest request) {
        log.info("Validation order request: {}", request);
        jmsTemplate.convertAndSend(JmsConfig.VALIDATION_RESULT_QUEUE,
                ValidationResult.builder()
                        .orderId(request.getOrder().getId())
                        .isValid(true)
                        .build());
    }
}
