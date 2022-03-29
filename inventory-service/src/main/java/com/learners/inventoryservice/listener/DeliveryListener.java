package com.learners.inventoryservice.listener;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.repository.InventoryRepository;
import com.learners.model.events.DeliveryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.learners.inventoryservice.config.JmsConfig.INVENTORY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final InventoryRepository inventoryRepository;

    @JmsListener(destination = INVENTORY_QUEUE)
    public void receiveDelivery(DeliveryEvent event) {
        log.debug("Delivery event arrived: {}", event);
        Inventory inventory = Inventory.builder()
                .pizzaId(event.getPizzaId())
                .inventoryOnHand(event.getQuantity())
                .build();
        inventoryRepository.save(inventory);
    }
}
