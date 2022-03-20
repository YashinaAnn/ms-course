package com.learners.pizzaservice.service.delivery.impl;

import com.learners.model.events.DeliveryEvent;
import com.learners.model.events.PizzaDeliveryEvent;
import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.service.delivery.DeliveryEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.learners.pizzaservice.config.JmsConfig.DELIVERY_REQUEST_QUEUE;
import static com.learners.pizzaservice.config.JmsConfig.NEW_INVENTORY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventListenerImpl implements DeliveryEventListener {

    private final JmsTemplate jmsTemplate;
    private final AppsConfigs configs;

    @Override
    @Transactional
    @JmsListener(destination = DELIVERY_REQUEST_QUEUE)
    public void listen(PizzaDeliveryEvent event) {
        log.debug("Delivery event arrived: {}", event);
        DeliveryEvent deliveryEvent = DeliveryEvent.builder()
                .pizzaId(event.getPizza().getId())
                .name(event.getPizza().getName())
                .quantity(configs.getQuantityToDeliver())
                .build();
        log.debug("Sending inventory request: {}", deliveryEvent);
        jmsTemplate.convertAndSend(NEW_INVENTORY_QUEUE, deliveryEvent);
    }
}
