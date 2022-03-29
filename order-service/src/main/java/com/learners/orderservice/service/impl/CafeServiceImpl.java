package com.learners.orderservice.service.impl;

import com.learners.model.OrderStatus;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.dto.PizzaDto;
import com.learners.orderservice.controller.OrderController;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.CafeService;
import com.learners.orderservice.service.PizzaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Profile("!test")
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
                .quantityOrdered(1)
                .build();

        Set<OrderLineDto> orderLines = new HashSet<>();
        orderLines.add(orderLineDto);
        return OrderDto.builder()
                .orderStatus(OrderStatus.NEW.name())
                .orderLines(orderLines)
                .build();
    }

    private <T> T randomEntity(List<T> list) {
        return list.get(random.nextInt(list.size() - 1));
    }
}
