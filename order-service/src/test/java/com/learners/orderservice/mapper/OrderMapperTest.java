package com.learners.orderservice.mapper;

import com.learners.orderservice.BaseTest;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.model.dto.order.OrderDto;
import com.learners.model.dto.order.OrderLineDto;
import com.learners.model.dto.PizzaDto;
import com.learners.orderservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OrderMapperTest extends BaseTest {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @MockBean
    RestTemplate restTemplate;

    @Test
    public void testOrderToDto() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order = getValidOrder(customer);
        OrderLine orderLine = getValidOrderLine(order);
        Set<OrderLine> orderLines = new HashSet<>();
        orderLines.add(orderLine);
        order.setOrderLines(orderLines);

        when(restTemplate.getForEntity(
                configs.getPizzaServiceHost() + configs.getPizzaServicePath() + orderLine.getPizzaId(),
                PizzaDto.class)
        ).thenReturn(ResponseEntity.ok(getPizza(orderLine.getPizzaId())));

        OrderDto dto = orderMapper.orderToDto(order);

        assertThat(order.getCustomer().getId()).isEqualTo(dto.getCustomerId());
        assertThat(order.getId()).isEqualTo(dto.getId());

        OrderLineDto orderLineDto = (OrderLineDto) dto.getOrderLines().toArray()[0];
        assertThat(orderLine.getOrder()).isEqualTo(order);
        assertThat(orderLine.getOrderQty()).isEqualTo(orderLineDto.getQuantity());
        assertThat(orderLine.getPizzaId()).isEqualTo(orderLineDto.getPizzaId());
    }

    @Test
    public void testDtoToOrder() {
        Customer customer = customerRepository.save(getValidCustomer());
        OrderDto dto = getValidOrderDto();
        dto.setCustomerId(customer.getId());

        Order order = orderMapper.dtoToOrder(dto);

        assertThat(order.getCustomer()).isEqualTo(customer);
        assertThat(order.getCustomer().getId()).isEqualTo(dto.getCustomerId());
        assertThat(order.getId()).isEqualTo(dto.getId());

        OrderLine orderLine = (OrderLine) order.getOrderLines().toArray()[0];
        OrderLineDto orderLineDto = (OrderLineDto) dto.getOrderLines().toArray()[0];
        assertThat(orderLine.getOrder()).isEqualTo(order);
        assertThat(orderLine.getOrderQty()).isEqualTo(orderLineDto.getQuantity());
        assertThat(orderLine.getPizzaId()).isEqualTo(orderLineDto.getPizzaId());
    }

    private PizzaDto getPizza(long pizzaId) {
        return PizzaDto.builder()
                .id(pizzaId)
                .name("Test Pizza")
                .build();
    }
}
