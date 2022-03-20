package com.learners.orderservice.integration;

import com.learners.orderservice.BaseTest;
import com.learners.orderservice.controller.OrderController;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.orderservice.model.dto.OrderDto;
import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class OrderIntegrationNegativeTest extends BaseTest {

    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @MockBean
    private RestTemplate restTemplate;

    private static final UUID NON_EXISTING_CUSTOMER_ID = UUID.randomUUID();
    private static final UUID NON_EXISTING_ORDER_ID = UUID.randomUUID();

    private PizzaDto pizzaDto = getValidPizzaDto();

    @BeforeEach
    public void setup() {
        pizzaDto = getValidPizzaDto();
        when(restTemplate.getForEntity(configs.getPizzaServiceHost() + configs.getPizzaServicePath() + PIZZA_ID,
                PizzaDto.class)
        ).thenReturn(new ResponseEntity<>(pizzaDto, HttpStatus.OK));
    }

    @Test
    public void testGetOrders_NonExistingCustomerId() {
        assertThrows(CustomerNotFoundException.class,
                () -> orderController.getOrders(NON_EXISTING_CUSTOMER_ID, Optional.empty(), Optional.empty()));
    }

    @Test
    public void testGetOrderById_NonExistingCustomerId() {
        assertThrows(CustomerNotFoundException.class,
                () -> orderController.getOrderById(NON_EXISTING_CUSTOMER_ID, NON_EXISTING_ORDER_ID));
    }

    @Test
    public void testGetOrderById_NonExistingOrderId() {
        Customer customer = customerRepository.save(getValidCustomer());
        assertThrows(OrderNotFoundException.class,
                () -> orderController.getOrderById(customer.getId(), NON_EXISTING_ORDER_ID));
    }

    @Test
    public void testGetOrderById_NoPermissions() {
        Customer orderCustomer = customerRepository.save(getValidCustomer());
        OrderDto orderDto = getValidOrderDto();
        orderDto.setCustomerId(null);
        ResponseEntity<OrderDto> response = orderController.placeOrder(orderCustomer.getId(), orderDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        UUID orderId = response.getBody().getId();
        Customer customer = customerRepository.save(getValidCustomer());


        assertThrows(OrderNotFoundException.class,
                () -> orderController.getOrderById(customer.getId(), orderId));
    }

    @Test
    public void testPlaceOrder_NonExistingCustomerId() {
        OrderDto orderDto = getValidOrderDto();
        orderDto.setCustomerId(null);

        assertThrows(CustomerNotFoundException.class,
                () -> orderController.placeOrder(NON_EXISTING_CUSTOMER_ID, getValidOrderDto()));
    }
}