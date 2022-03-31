package com.learners.orderservice.sm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learners.model.OrderStatus;
import com.learners.model.dto.PizzaDto;
import com.learners.orderservice.BaseTest;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.OrderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(WireMockExtension.class)
public class OrderManagerSmTest extends BaseTest {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private AppConfigs configs;

    @TestConfiguration
    static class RestTemplateBuilderProvider {
        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer(){
            WireMockServer server = with(wireMockConfig().port(8083));
            server.start();
            return server;
        }
    }

    @Test
    public void testNewToAllocated() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATED);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ALLOCATED);
        savedOrder.getOrderLines().forEach(orderLine -> {
            assertThat(orderLine.getQtyAllocated()).isEqualTo(orderLine.getOrderQty());
        });
    }

    @Test
    public void testNewToPickedUp() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATED);

        orderManager.pickUp(order.getId());
        awaitForStatus(order.getId(), OrderStatus.PICKED_UP);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PICKED_UP);
    }

    private Order createOrder() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order = Order.builder()
                .customer(customer)
                .build();
        Set<OrderLine> lines = new HashSet<>();
        lines.add(OrderLine.builder()
                .pizzaId(PIZZA_ID)
                .orderQty(1)
                .order(order)
                .build());
        order.setOrderLines(lines);
        return order;
    }

    private void awaitForStatus(UUID orderId, OrderStatus status) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                    Order savedOrder = orderRepository.findById(orderId)
                            .orElseThrow(IllegalArgumentException::new);
                    return status.equals(savedOrder.getOrderStatus());
                });
    }
}
