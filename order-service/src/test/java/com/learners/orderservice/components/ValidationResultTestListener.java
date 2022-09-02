package com.learners.orderservice.components;

import com.learners.model.dto.order.OrderDto;
import com.learners.model.events.ValidateOrderRequest;
import com.learners.model.events.ValidationResult;
import com.learners.orderservice.config.AppConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.learners.orderservice.BaseTest.NO_VALIDATION;
import static com.learners.orderservice.BaseTest.VALIDATION_ERROR;

@Slf4j
@Profile("test")
@Component
@RequiredArgsConstructor
public class ValidationResultTestListener {

    private final JmsTemplate jmsTemplate;
    private final AppConfigs configs;

    @JmsListener(destination = "${app.config.validate-order-queue}")
    public void listen(ValidateOrderRequest request) {
        log.info("Validation order request: {}", request);
        OrderDto order = request.getOrder();

        if (skipValidation(order)) {
            log.info("Skipping validation...");
            return;
        }

        jmsTemplate.convertAndSend(configs.getValidationResultQueue(),
                ValidationResult.builder()
                        .orderId(request.getOrder().getId())
                        .isValid(isValidOrder(order))
                        .build());
    }

    private boolean skipValidation(OrderDto order) {
        return order.getOrderLines().stream()
                .anyMatch(orderLine -> NO_VALIDATION.equals(orderLine.getPizzaName()));
    }

    private boolean isValidOrder(OrderDto order) {
        return order.getOrderLines().stream()
                .noneMatch(orderLine -> VALIDATION_ERROR.equals(orderLine.getPizzaName()));
    }
}
