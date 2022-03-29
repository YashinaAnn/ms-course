package com.learners.inventoryservice.service;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.repository.InventoryRepository;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class AllocationServiceTest {

    @Autowired
    private AllocationService allocationService;
    @Autowired
    private InventoryRepository repository;

    @Transactional
    @Test
    public void testFullAllocation() {
        long pizzaId = 123L;
        int qtyOrdered = 2;
        int inventoryOnHand = 10;
        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(getValidOrderLineDto(pizzaId, qtyOrdered));
        OrderDto orderDto = getValidOrderDto(orderLines);
        Inventory inventory = repository.save(getValidInventory(pizzaId, inventoryOnHand));

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(true);
        inventory = repository.findById(inventory.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(inventory.getInventoryOnHand()).isEqualTo(inventoryOnHand - qtyOrdered);
        assertThat(orderDto.getOrderLines().stream().map(OrderLineDto::getQuantityAllocated)).contains(qtyOrdered);
    }

    @Transactional
    @Test
    public void testPartialAllocation_AllocationCompleted() {
        long pizzaId = 123L;
        int qtyOrdered = 2;
        int inv1 = 1;
        int inv2 = 10;

        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(getValidOrderLineDto(pizzaId, qtyOrdered));
        OrderDto orderDto = getValidOrderDto(orderLines);
        Inventory inventory1 = repository.save(getValidInventory(pizzaId, inv1));
        Inventory inventory2 = repository.save(getValidInventory(pizzaId, inv2));

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(true);
        assertThat(repository.findById(inventory1.getId())).isEmpty();
        inventory2 = repository.findById(inventory2.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(inventory2.getInventoryOnHand()).isEqualTo(inv2 - inv1);
        assertThat(orderDto.getOrderLines().stream().map(OrderLineDto::getQuantityAllocated)).contains(qtyOrdered);
    }

    @Transactional
    @Test
    public void testPartialAllocation_PendingInventory() {
        long pizzaId = 123L;
        int qtyOrdered = 2;
        int inv1 = 1;

        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(getValidOrderLineDto(pizzaId, qtyOrdered));
        OrderDto orderDto = getValidOrderDto(orderLines);
        Inventory inventory1 = repository.save(getValidInventory(pizzaId, inv1));

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(false);
        assertThat(repository.findById(inventory1.getId())).isEmpty();
        assertThat(orderDto.getOrderLines().stream().map(OrderLineDto::getQuantityAllocated)).contains(inv1);
    }

    private Inventory getValidInventory(long pizzaId, int inventory) {
        return Inventory.builder()
                .pizzaId(pizzaId)
                .inventoryOnHand(inventory)
                .build();
    }

    private OrderDto getValidOrderDto(Set<OrderLineDto> orderLines) {
        return OrderDto.builder()
                .customerId(UUID.randomUUID())
                .orderStatus("NEW")
                .orderLines(orderLines)
                .build();
    }

    private OrderLineDto getValidOrderLineDto(long pizzaId, int qtyOrdered) {
        return OrderLineDto.builder()
                .pizzaId(pizzaId)
                .pizzaName("Great Pizza Combo")
                .quantityOrdered(qtyOrdered)
                .build();
    }
}
