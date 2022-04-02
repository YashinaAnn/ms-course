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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class AllocationServiceTest {

    @Autowired
    private AllocationService allocationService;
    @Autowired
    private InventoryRepository repository;

    public final long PIZZA_ID = 123L;

    @Transactional
    @Test
    public void testFullAllocation() {
        int qtyOrdered = 2;
        int inventoryOnHand = 10;

        Inventory inventory = repository.save(getValidInventory(PIZZA_ID, inventoryOnHand));

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        OrderDto orderDto = createOrder(orderLine);

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(true);
        inventory = repository.findById(inventory.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(inventory.getInventoryOnHand()).isEqualTo(inventoryOnHand - qtyOrdered);
        assertThat(orderDto.getOrderLines().stream().map(OrderLineDto::getQuantityAllocated)).contains(qtyOrdered);
    }

    @Transactional
    @Test
    public void testFullAllocation_EqualsToOrdered() {
        int qtyOrdered = 2;
        int inventoryOnHand = 2;

        Inventory inventory = repository.save(getValidInventory(PIZZA_ID, inventoryOnHand));

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        OrderDto orderDto = createOrder(orderLine);

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(true);
        assertThat(repository.findById(inventory.getId())).isEmpty();
    }

    @Transactional
    @Test
    public void testPartialAllocation_AllocationCompleted() {
        int qtyOrdered = 2;
        int inv1 = 1;
        int inv2 = 10;

        Inventory inventory1 = repository.save(getValidInventory(PIZZA_ID, inv1));
        Inventory inventory2 = repository.save(getValidInventory(PIZZA_ID, inv2));

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        OrderDto orderDto = createOrder(orderLine);

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
        int qtyOrdered = 2;
        int inventoryOnHand = 1;

        Inventory inventory1 = repository.save(getValidInventory(PIZZA_ID, inventoryOnHand));

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        OrderDto orderDto = createOrder(orderLine);

        boolean allocated = allocationService.allocateOrder(orderDto);

        assertThat(allocated).isEqualTo(false);
        assertThat(repository.findById(inventory1.getId())).isEmpty();
        assertThat(orderDto.getOrderLines().stream().map(OrderLineDto::getQuantityAllocated))
                .contains(inventoryOnHand);
    }

    @Transactional
    @Test
    void testDeallocateOrder_FullyAllocated() {
        int qtyOrdered = 2;
        int qtyAllocated = 2;

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        orderLine.setQuantityAllocated(qtyAllocated);
        OrderDto orderDto = createOrder(orderLine);

        allocationService.deallocateOrder(orderDto);

        List<Inventory> inventoryList = repository.findAllByPizzaId(PIZZA_ID);
        assertThat(inventoryList).hasSize(1);
        assertThat(inventoryList)
                .allMatch(inventory -> inventory.getInventoryOnHand() == qtyAllocated);
    }

    @Transactional
    @Test
    void testDeallocateOrder_PartiallyAllocated() {
        int qtyOrdered = 2;
        int qtyAllocated = 1;

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        orderLine.setQuantityAllocated(qtyAllocated);
        OrderDto orderDto = createOrder(orderLine);

        allocationService.deallocateOrder(orderDto);

        List<Inventory> inventoryList = repository.findAllByPizzaId(PIZZA_ID);
        assertThat(inventoryList).hasSize(1);
        assertThat(inventoryList)
                .allMatch(inventory -> inventory.getInventoryOnHand() == qtyAllocated);
    }

    @Transactional
    @Test
    void testDeallocateOrder_NotAllocated() {
        int qtyOrdered = 2;
        int qtyAllocated = 0;

        OrderLineDto orderLine = getValidOrderLineDto(PIZZA_ID, qtyOrdered);
        orderLine.setQuantityAllocated(qtyAllocated);
        OrderDto orderDto = createOrder(orderLine);

        allocationService.deallocateOrder(orderDto);

        List<Inventory> inventoryList = repository.findAllByPizzaId(PIZZA_ID);
        assertThat(inventoryList).isEmpty();
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

    private OrderDto createOrder(OrderLineDto... orderLineDtos) {
        Set<OrderLineDto> orderLines = new HashSet<>(Arrays.asList(orderLineDtos));
        return getValidOrderDto(orderLines);
    }

    private OrderLineDto getValidOrderLineDto(long pizzaId, int qtyOrdered) {
        return OrderLineDto.builder()
                .pizzaId(pizzaId)
                .pizzaName("Great Pizza Combo")
                .quantityOrdered(qtyOrdered)
                .build();
    }
}
