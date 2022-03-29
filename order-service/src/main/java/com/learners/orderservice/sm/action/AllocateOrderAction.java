package com.learners.orderservice.sm.action;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import com.learners.model.events.AllocateOrderRequest;
import com.learners.orderservice.config.JmsConfig;
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
@Qualifier("allocateOrderAction")
public class AllocateOrderAction implements Action<OrderStatus, OrderEvent> {

    private final JmsTemplate jmsTemplate;
    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader(ORDER_ID_HEADER);
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        AllocateOrderRequest request = AllocateOrderRequest.of(mapper.orderToDto(order));
        log.info("Sending allocate order request: {}", request);
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE, request);
    }
}
