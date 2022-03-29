package com.learners.pizzaservice.service.order;

import com.learners.model.events.ValidateOrderRequest;
import com.learners.model.events.ValidationResult;
import com.learners.pizzaservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidationListener {

    private final OrderValidator validator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest event) {
        log.info("Validate order request: {}", event);

        ValidationResult resultEvent = ValidationResult.builder()
                .orderId(event.getOrder().getId())
                .isValid(validator.validate(event.getOrder()))
                .build();

        log.info("Validation result: {}", resultEvent);
        jmsTemplate.convertAndSend(JmsConfig.VALIDATION_RESULT_QUEUE, resultEvent);
    }
}
