package com.learners.pizzaservice.service.delivery.impl;

import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.events.NewInventoryEvent;
import com.learners.pizzaservice.events.PizzaDeliveryEvent;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.service.delivery.DeliveryEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventListenerImpl implements DeliveryEventListener {

    public static final String DELIVERY_REQUEST_QUEUE = "delivery-request";
    public static final String NEW_INVENTORY_QUEUE = "inventory";

    private final JmsTemplate jmsTemplate;
    private final AppsConfigs configs;

    @Override
    @JmsListener(destination = DELIVERY_REQUEST_QUEUE)
    public void listen(PizzaDeliveryEvent event) {
        log.debug("Delivery event arrived: {}", event);
        PizzaDto pizza = event.getPizza().toBuilder().inventoryOnHand(configs.getQuantityToDeliver()).build();
        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(pizza);
        log.debug("Sending inventory request: {}", newInventoryEvent);
        jmsTemplate.convertAndSend(NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}
