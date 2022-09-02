package com.learners.pizzaservice.listener;

import com.learners.model.events.DeliveryEvent;
import com.learners.model.events.PizzaDeliveryEvent;
import com.learners.pizzaservice.config.AppsConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final JmsTemplate jmsTemplate;
    private final AppsConfigs configs;

    @Transactional
    @JmsListener(destination = "${app.config.delivery-request-queue}")
    public void listen(PizzaDeliveryEvent event) {
        log.debug("Delivery event arrived: {}", event);
        DeliveryEvent deliveryEvent = DeliveryEvent.builder()
                .pizzaId(event.getPizza().getId())
                .name(event.getPizza().getName())
                .quantity(configs.getQuantityToDeliver())
                .build();
        log.debug("Sending inventory request: {}", deliveryEvent);
        jmsTemplate.convertAndSend(configs.getNewInventoryQueue(), deliveryEvent);
    }
}
