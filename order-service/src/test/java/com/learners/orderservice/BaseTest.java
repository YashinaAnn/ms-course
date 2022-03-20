package com.learners.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.model.OrderStatusEnum;
import com.learners.orderservice.model.PizzaType;
import com.learners.orderservice.model.dto.OrderDto;
import com.learners.orderservice.model.dto.OrderLineDto;
import com.learners.orderservice.model.dto.PizzaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
public class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AppConfigs configs;

    public static final long PIZZA_ID = 1L;

    protected OrderDto getValidOrderDto() {
        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(getValidOrderLineDto());
        return OrderDto.builder()
                .customerId(UUID.randomUUID())
                .orderStatus(OrderStatusEnum.NEW)
                .orderLines(orderLines)
                .build();
    }

    protected OrderLineDto getValidOrderLineDto() {
        return OrderLineDto.builder()
                .pizzaId(PIZZA_ID)
                .pizzaName("Great Pizza Combo")
                .quantity(1)
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
                .orderStatus(OrderStatusEnum.NEW)
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
}
