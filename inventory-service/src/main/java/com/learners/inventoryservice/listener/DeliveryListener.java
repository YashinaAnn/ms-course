package com.learners.inventoryservice.listener;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.repository.InventoryRepository;
import com.learners.model.events.DeliveryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final InventoryRepository inventoryRepository;

    @JmsListener(destination = "${app.config.inventory-queue}")
    public void listen(DeliveryEvent event) {
        log.debug("Delivery event arrived: {}", event);
        Inventory inventory = Inventory.builder()
                .pizzaId(event.getPizzaId())
                .inventoryOnHand(event.getQuantity())
                .build();
        inventoryRepository.save(inventory);
    }
}
