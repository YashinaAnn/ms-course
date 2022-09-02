package com.learners.pizzaservice.service.impl;

import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.model.events.PizzaDeliveryEvent;
import com.learners.pizzaservice.mapper.PizzaMapper;
import com.learners.pizzaservice.repository.PizzaRepository;
import com.learners.pizzaservice.service.DeliveryService;
import com.learners.pizzaservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final PizzaRepository pizzaRepository;
    private final InventoryService inventoryService;
    private final JmsTemplate jmsTemplate;
    private final PizzaMapper mapper;
    private final AppsConfigs configs;

    @Override
    @Scheduled(fixedRate = 30000)
    public void checkAndAdjustInventory() {
        log.debug("Checking pizza inventory");
        pizzaRepository.findAll().forEach(
                pizza -> {
                    Optional<Integer> inventory = inventoryService.getInventoryByPizzaId(pizza.getId());
                    log.debug("Inventory response for pizza {} is {}", pizza.getName(), inventory);
                    if (inventory.isPresent() && inventory.get() < configs.getMinimalQuantity()) {
                        log.debug("Triggering delivery request for pizza {}", pizza.getName());
                        jmsTemplate.convertAndSend(configs.getDeliveryRequestQueue(),
                                new PizzaDeliveryEvent(mapper.pizzaToDto(pizza)));
                    }
                }
        );
    }
}
