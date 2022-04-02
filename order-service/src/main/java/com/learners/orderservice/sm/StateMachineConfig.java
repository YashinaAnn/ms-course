package com.learners.orderservice.sm;

import com.learners.model.OrderEvent;
import com.learners.model.OrderStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    private final Action<OrderStatus, OrderEvent> validateOrderAction;
    private final Action<OrderStatus, OrderEvent> allocateOrderAction;
    private final Action<OrderStatus, OrderEvent> validationErrorAction;
    private final Action<OrderStatus, OrderEvent> allocationErrorAction;
    private final Action<OrderStatus, OrderEvent> deallocateOrderAction;

    public StateMachineConfig(@Qualifier("validateOrderAction") Action<OrderStatus, OrderEvent> validateOrderAction,
                              @Qualifier("allocateOrderAction") Action<OrderStatus, OrderEvent> allocateOrderAction,
                              @Qualifier("validationErrorAction") Action<OrderStatus, OrderEvent> validationErrorAction,
                              @Qualifier("deallocateOrderAction") Action<OrderStatus, OrderEvent> deallocateOrderAction,
                              @Qualifier("allocationErrorAction") Action<OrderStatus, OrderEvent> allocationErrorAction) {
        this.validateOrderAction = validateOrderAction;
        this.allocateOrderAction = allocateOrderAction;
        this.validationErrorAction = validationErrorAction;
        this.allocationErrorAction = allocationErrorAction;
        this.deallocateOrderAction = deallocateOrderAction;
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.NEW)
                .states(EnumSet.allOf(OrderStatus.class))
                .end(OrderStatus.VALIDATION_ERROR)
                .end(OrderStatus.ALLOCATION_ERROR)
                .end(OrderStatus.PICKED_UP)
                .end(OrderStatus.CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStatus.NEW).target(OrderStatus.VALIDATION_PENDING)
                .event(OrderEvent.VALIDATE_ORDER)
                .action(validateOrderAction)
            .and().withExternal()
                .source(OrderStatus.VALIDATION_PENDING).target(OrderStatus.VALIDATED)
                .event(OrderEvent.VALIDATION_SUCCESS)
            .and().withExternal()
                .source(OrderStatus.VALIDATION_PENDING).target(OrderStatus.VALIDATION_ERROR)
                .event(OrderEvent.VALIDATION_FAILED)
                .action(validationErrorAction)
            .and().withExternal()
                .source(OrderStatus.VALIDATION_PENDING).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
            .and().withExternal()
                .source(OrderStatus.VALIDATED).target(OrderStatus.ALLOCATION_PENDING)
                .event(OrderEvent.ALLOCATE_ORDER)
                .action(allocateOrderAction)
            .and().withExternal()
                .source(OrderStatus.VALIDATED).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
            .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.ALLOCATED)
                .event(OrderEvent.ALLOCATION_SUCCESS)
            .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.ALLOCATION_ERROR)
                .event(OrderEvent.ALLOCATION_FAILED)
                .action(allocationErrorAction)
            .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.PENDING_INVENTORY)
                .event(OrderEvent.ALLOCATION_NO_INVENTORY)
            .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
            .and().withExternal()
                .source(OrderStatus.PENDING_INVENTORY).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .action(deallocateOrderAction)
            .and().withExternal()
                .source(OrderStatus.ALLOCATED).target(OrderStatus.PICKED_UP)
                .event(OrderEvent.PICK_UP)
            .and().withExternal()
                .source(OrderStatus.ALLOCATED).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .action(deallocateOrderAction);
    }
}
