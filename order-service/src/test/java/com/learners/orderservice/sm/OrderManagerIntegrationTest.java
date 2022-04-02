package com.learners.orderservice.sm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learners.model.OrderStatus;
import com.learners.model.dto.PizzaDto;
import com.learners.model.events.AllocationErrorEvent;
import com.learners.model.events.DeallocateOrderRequest;
import com.learners.orderservice.BaseTest;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.config.JmsConfig;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.OrderManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

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

@Slf4j
@ExtendWith(WireMockExtension.class)
public class OrderManagerIntegrationTest extends BaseTest {

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
    @Autowired
    private JmsTemplate jmsTemplate;

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
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(getValidPizzaDto()))));

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
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(getValidPizzaDto()))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATED);

        orderManager.pickUp(order.getId());
        awaitForStatus(order.getId(), OrderStatus.PICKED_UP);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PICKED_UP);
    }

    @Test
    public void testNewToValidationError() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        pizzaDto.setName(VALIDATION_ERROR);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.VALIDATION_ERROR);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.VALIDATION_ERROR);
    }

    @Test
    public void testNewToAllocationError() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        pizzaDto.setName(ALLOCATION_ERROR);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATION_ERROR);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ALLOCATION_ERROR);
        savedOrder.getOrderLines().forEach(orderLine ->
            assertThat(orderLine.getQtyAllocated()).isEqualTo(0)
        );

        AllocationErrorEvent event = (AllocationErrorEvent) jmsTemplate.receiveAndConvert(JmsConfig.ALLOCATION_ERROR_QUEUE);
        assertThat(event).isNotNull();
        assertThat(event.getOrderId()).isEqualTo(order.getId());
    }

    @Test
    public void testNewToPendingInventory() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        pizzaDto.setName(PENDING_INVENTORY);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.PENDING_INVENTORY);

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PENDING_INVENTORY);
        boolean pendingInventory = savedOrder.getOrderLines().stream().anyMatch(orderLine ->
            orderLine.getQtyAllocated() != orderLine.getOrderQty()
        );
        assertThat(pendingInventory).isTrue();
    }

    @Test
    public void testValidationPendingToCancelled() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        pizzaDto.setName(NO_VALIDATION);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.VALIDATION_PENDING);

        orderManager.cancel(order.getId());
        awaitForStatus(order.getId(), OrderStatus.CANCELLED);
    }

    @Test
    public void testAllocationPendingToCancelled() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        pizzaDto.setName(NO_ALLOCATION);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATION_PENDING);

        orderManager.cancel(order.getId());
        awaitForStatus(order.getId(), OrderStatus.CANCELLED);
    }

    @Test
    public void testAllocatedToCancelled() throws JsonProcessingException {
        PizzaDto pizzaDto = getValidPizzaDto();
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + PIZZA_ID)
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaDto))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.ALLOCATED);

        orderManager.cancel(order.getId());
        awaitForStatus(order.getId(), OrderStatus.CANCELLED);

        DeallocateOrderRequest request = (DeallocateOrderRequest) jmsTemplate.receiveAndConvert(JmsConfig.DEALLOCATE_ORDER_QUEUE);
        assertThat(request).isNotNull();
        assertThat(request.getOrder().getId()).isEqualTo(order.getId());
    }

    @Test
    public void testPendingInventoryToCancelled() throws JsonProcessingException {
        PizzaDto pizzaNoInventory = getValidPizzaDto();
        pizzaNoInventory.setName(PENDING_INVENTORY);
        wireMockServer.stubFor(get(configs.getPizzaServicePath() + pizzaNoInventory.getId())
                .willReturn(okJson(objectMapper.writeValueAsString(pizzaNoInventory))));

        Order order = orderManager.createOrder(createOrder());
        awaitForStatus(order.getId(), OrderStatus.PENDING_INVENTORY);

        orderManager.cancel(order.getId());
        awaitForStatus(order.getId(), OrderStatus.CANCELLED);

        DeallocateOrderRequest request = (DeallocateOrderRequest) jmsTemplate.receiveAndConvert(JmsConfig.DEALLOCATE_ORDER_QUEUE);
        assertThat(request).isNotNull();
        assertThat(request.getOrder().getId()).isEqualTo(order.getId());
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
                    log.info("Current status: {}, Expected status: {}", savedOrder.getOrderStatus(), status);
                    return status.equals(savedOrder.getOrderStatus());
                });
    }
}
