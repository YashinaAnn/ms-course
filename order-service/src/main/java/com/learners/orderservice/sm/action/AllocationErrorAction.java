package com.learners.orderservice.sm.action;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import com.learners.model.events.AllocationErrorEvent;
import com.learners.orderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.learners.orderservice.service.impl.OrderManagerImpl.ORDER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@Qualifier("allocationErrorAction")
@Component
public class AllocationErrorAction implements Action<OrderStatus, OrderEvent> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader(ORDER_ID_HEADER);

        AllocationErrorEvent event = AllocationErrorEvent.builder().orderId(orderId).build();
        log.info("Sending allocation error event: {}", event);
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATION_ERROR_QUEUE, event);
    }
}
