package com.learners.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.model.OrderStatus;
import com.learners.model.PizzaType;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.dto.PizzaDto;
import com.learners.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
public class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AppConfigs configs;
    @Autowired
    protected OrderRepository orderRepository;

    public static final long PIZZA_ID = 1L;

    protected OrderDto getValidOrderDto() {
        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(getValidOrderLineDto());
        return OrderDto.builder()
                .customerId(UUID.randomUUID())
                .orderStatus(OrderStatus.NEW.name())
                .orderLines(orderLines)
                .build();
    }

    protected OrderLineDto getValidOrderLineDto() {
        return OrderLineDto.builder()
                .pizzaId(PIZZA_ID)
                .pizzaName("Great Pizza Combo")
                .quantityOrdered(1)
                .build();
    }

    protected Customer getValidCustomer() {
        return Customer.builder()
                .firstName("Ivan")
                .lastName("Kuzmin")
                .phoneNumber("+81231421231")
                .build();
    }

    protected Order getValidOrder(Customer customer) {
        return Order.builder()
                .customer(customer)
                .orderStatus(OrderStatus.NEW)
                .build();
    }

    protected OrderLine getValidOrderLine(Order order) {
        return OrderLine.builder()
                .pizzaId(PIZZA_ID)
                .qtyAllocated(0)
                .orderQty(1)
                .order(order)
                .build();
    }

    protected PizzaDto getValidPizzaDto() {
        return PizzaDto.builder()
                .id(PIZZA_ID)
                .name("Test pizza")
                .type(PizzaType.CHEEZ)
                .upc("1234")
                .price(BigDecimal.TEN)
                .build();
    }

    protected Order saveOrder(Customer customer) {
        Order order = getValidOrder(customer);
        OrderLine orderLine = getValidOrderLine(order);
        Set<OrderLine> orderLines = new HashSet<>();
        orderLines.add(orderLine);
        order.setOrderLines(orderLines);
        return orderRepository.save(order);
    }
}
