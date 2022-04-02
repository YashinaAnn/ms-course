package com.learners.orderservice.sm.action;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.learners.orderservice.service.impl.OrderManagerImpl.ORDER_ID_HEADER;

@Slf4j
@Component
@Qualifier("validationErrorAction")
public class ValidationErrorAction implements Action<OrderStatus, OrderEvent> {

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader(ORDER_ID_HEADER);
        log.error("Validation error for order: {}", orderId);
    }
}
