package com.learners.orderservice.integration;

import com.learners.orderservice.BaseTest;
import com.learners.orderservice.controller.OrderController;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.mapper.OrderMapper;
import com.learners.orderservice.model.dto.OrderDto;
import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OrderIntegrationPositiveTest extends BaseTest {

    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderMapper orderMapper;
    @MockBean
    private RestTemplate restTemplate;

    private PizzaDto pizzaDto = getValidPizzaDto();

    @BeforeEach
    public void setup() {
        pizzaDto = getValidPizzaDto();
        when(restTemplate.getForEntity(configs.getPizzaServiceHost() + configs.getPizzaServicePath() + PIZZA_ID,
                PizzaDto.class)
        ).thenReturn(new ResponseEntity<>(pizzaDto, HttpStatus.OK));
    }

    @Test
    public void testGetOrderById() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order1 = saveOrder(customer);

        ResponseEntity<OrderDto> response = orderController.getOrderById(customer.getId(), order1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(orderMapper.orderToDto(order1));
    }

    @Test
    public void testGetOrders() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order1 = saveOrder(customer);
        Order order2 = saveOrder(customer);

        Customer anotherCustomer = customerRepository.save(getValidCustomer());
        saveOrder(anotherCustomer);

        ResponseEntity<Page<OrderDto>> response = orderController.getOrders(customer.getId(), Optional.empty(), Optional.empty());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<OrderDto> list = response.getBody();
        assertThat(list).isNotNull();
        assertThat(list.getContent()).hasSize(2);
        assertThat(list.getContent()).contains(orderMapper.orderToDto(order1), orderMapper.orderToDto(order2));

        assertThat(list.getNumber()).isEqualTo(configs.getDefaultPage());
        assertThat(list.getSize()).isEqualTo(configs.getDefaultSize());
    }

    @Test
    public void testGetOrders_Pagination() {
        Customer customer = customerRepository.save(getValidCustomer());
        saveOrder(customer);

        ResponseEntity<Page<OrderDto>> response = orderController.getOrders(customer.getId(), Optional.of(1), Optional.of(2));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<OrderDto> list = response.getBody();
        assertThat(list).isNotNull();
        assertThat(list.getContent()).isEmpty();
    }

    @Test
    public void testGetOrders_Pagination_FirstPage() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order = saveOrder(customer);
        saveOrder(customer);

        ResponseEntity<Page<OrderDto>> response = orderController.getOrders(customer.getId(), Optional.of(0), Optional.of(1));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<OrderDto> list = response.getBody();
        assertThat(list).isNotNull();
        assertThat(list.getContent()).hasSize(1);
        assertThat(list.getContent()).contains(orderMapper.orderToDto(order));

        assertThat(list.getNumber()).isEqualTo(0);
        assertThat(list.getSize()).isEqualTo(1);
    }

    @Test
    public void testGetOrders_Pagination_SecondPage() {
        Customer customer = customerRepository.save(getValidCustomer());
        saveOrder(customer);
        Order order = saveOrder(customer);

        ResponseEntity<Page<OrderDto>> response = orderController.getOrders(customer.getId(), Optional.of(1), Optional.of(1));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<OrderDto> list = response.getBody();
        assertThat(list).isNotNull();
        assertThat(list.getContent()).hasSize(1);
        assertThat(list.getContent()).contains(orderMapper.orderToDto(order));

        assertThat(list.getNumber()).isEqualTo(1);
        assertThat(list.getSize()).isEqualTo(1);
    }

    @Test
    public void testPlaceOrder() {
        Customer customer = customerRepository.save(getValidCustomer());
        ResponseEntity<OrderDto> response = orderController.placeOrder(customer.getId(), getValidOrderDto());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        UUID orderId = response.getBody().getId();
        assertThat(response.getBody()).isEqualTo(orderMapper.orderToDto(orderRepository.findById(orderId).get()));
    }

    private Order saveOrder(Customer customer) {
        Order order = getValidOrder(customer);
        OrderLine orderLine = getValidOrderLine(order);
        Set<OrderLine> orderLines = new HashSet<>();
        orderLines.add(orderLine);
        order.setOrderLines(orderLines);
        return orderRepository.save(order);
    }
}