package com.learners.inventoryservice.service;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.repository.InventoryRepository;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final InventoryRepository repository;

    @Override
    public boolean allocateOrder(OrderDto order) {
        log.debug("Allocating inventory for order: {}", order);

        int totalAllocated = 0;
        int totalOrdered = 0;

        for (OrderLineDto orderLine: order.getOrderLines()) {
            if (orderLine.getQuantityAllocated() < orderLine.getQuantityOrdered()) {
                allocateOrderLine(orderLine);
            }
            totalAllocated += orderLine.getQuantityAllocated();
            totalOrdered += orderLine.getQuantityOrdered();
        }
        log.debug("Total ordered: {}, Total allocated: {}", totalOrdered, totalAllocated);
        return totalAllocated == totalOrdered;
    }

    private void allocateOrderLine(OrderLineDto orderLine) {
        List<Inventory> inventoryList = repository.findAllByPizzaId(orderLine.getPizzaId());
        for (Inventory inventory: inventoryList) {
            int inventoryToAllocate = orderLine.getQuantityOrdered() - orderLine.getQuantityAllocated();

            if (inventory.getInventoryOnHand() >= inventoryToAllocate) {
                fullAllocation(inventory, orderLine);
                return;
            } else if (inventory.getInventoryOnHand() > 0) {
                partialAllocation(inventory, orderLine);
            }
        }
    }

    private void fullAllocation(Inventory inventory, OrderLineDto orderLine) {
        int inventoryToAllocate = orderLine.getQuantityOrdered() - orderLine.getQuantityAllocated();
        orderLine.setQuantityAllocated(orderLine.getQuantityOrdered());
        inventory.setInventoryOnHand(inventory.getInventoryOnHand() - inventoryToAllocate);
        repository.save(inventory);
    }

    private void partialAllocation(Inventory inventory, OrderLineDto orderLine) {
        orderLine.setQuantityAllocated(orderLine.getQuantityAllocated() + inventory.getInventoryOnHand());
        inventory.setInventoryOnHand(0);
        repository.delete(inventory);
    }
}
