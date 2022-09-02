package com.learners.orderservice.sm.action;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import com.learners.model.events.ValidateOrderRequest;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.mapper.OrderMapper;
import com.learners.orderservice.repository.OrderRepository;
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
@Component
@RequiredArgsConstructor
@Qualifier("validateOrderAction")
public class ValidateOrderAction implements Action<OrderStatus, OrderEvent> {

    private final JmsTemplate jmsTemplate;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final AppConfigs configs;

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader(ORDER_ID_HEADER);
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        ValidateOrderRequest event = ValidateOrderRequest.of(mapper.orderToDto(order));

        log.info("Sending validation order event: {}", event);
        jmsTemplate.convertAndSend(configs.getValidateOrderQueue(), event);
    }
}
