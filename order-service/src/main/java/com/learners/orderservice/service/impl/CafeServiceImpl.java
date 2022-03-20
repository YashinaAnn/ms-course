package com.learners.orderservice.service.impl;

import com.learners.orderservice.controller.OrderController;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.model.OrderStatusEnum;
import com.learners.orderservice.model.dto.OrderDto;
import com.learners.orderservice.model.dto.OrderLineDto;
import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.CafeService;
import com.learners.orderservice.service.PizzaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

//@Profile("demo")
@Service
@Slf4j
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

    private final OrderController orderController;
    private final CustomerRepository customerRepository;
    private final PizzaService pizzaService;
    private final Random random = new Random();

    @Override
    @Scheduled(fixedRate = 5000)
    public void open() {
        log.debug("New customer arrived!!!");
        Customer customer = getCustomer();
        PizzaDto pizza = getPizza();
        OrderDto orderDto = formatOrder(pizza);
        log.debug("Placing order by customer {}", customer.getId());
        orderController.placeOrder(customer.getId(), orderDto);
    }

    private Customer getCustomer() {
        return randomEntity(customerRepository.findAll());
    }

    private PizzaDto getPizza() {
        return randomEntity(pizzaService.getPizzaList().getContent());
    }

    private OrderDto formatOrder(PizzaDto pizza) {
        OrderLineDto orderLineDto = OrderLineDto.builder()
                .pizzaId(pizza.getId())
                .pizzaName(pizza.getName())
                .quantity(1)
                .build();

        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(orderLineDto);
        return OrderDto.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .orderLines(orderLines)
                .build();
    }

    private <T> T randomEntity(List<T> list) {
        return list.get(random.nextInt(list.size() - 1));
    }
}
