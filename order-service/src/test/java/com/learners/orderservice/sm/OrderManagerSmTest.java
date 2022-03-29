package com.learners.orderservice.sm;

import com.learners.model.OrderStatus;
import com.learners.orderservice.BaseTest;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderManagerSmTest extends BaseTest {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private CustomerRepository customerRepository;

    //@Test
    public void testNewOrder() {
        Customer customer = customerRepository.save(getValidCustomer());
        Order order = getValidOrder(customer);

        order = orderManager.createOrder(order);
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.NEW);
    }
}
