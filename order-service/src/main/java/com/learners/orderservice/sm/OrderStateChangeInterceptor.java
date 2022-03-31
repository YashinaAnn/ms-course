package com.learners.orderservice.sm;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

import static com.learners.orderservice.service.impl.OrderManagerImpl.ORDER_ID_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderStatus, OrderEvent> {

    private final OrderRepository repository;

    @Transactional
    @Override
    public void preStateChange(State<OrderStatus, OrderEvent> state, Message<OrderEvent> message,
                                Transition<OrderStatus, OrderEvent> transition, StateMachine<OrderStatus, OrderEvent> stateMachine, StateMachine<OrderStatus, OrderEvent> rootStateMachine) {
        UUID orderId = (UUID) Objects.requireNonNull(message).getHeaders().get(ORDER_ID_HEADER);
        log.info("Changing state of order {} to {}", orderId, state.getId());

        Order order = repository.findById(Objects.requireNonNull(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setOrderStatus(state.getId());
        repository.saveAndFlush(order);
    }
}
