package com.learners.orderservice.mapper;

import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.entity.Order;
import com.learners.orderservice.entity.OrderLine;
import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.model.dto.OrderDto;
import com.learners.orderservice.repository.CustomerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = OrderLineMapperImpl.class, builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

    @Autowired
    private CustomerRepository customerRepository;

    @Mapping(source = "order.customer", target = "customerId")
    public abstract OrderDto orderToDto(Order order);
    public abstract Order dtoToOrder(OrderDto dto);

    public UUID customerToCustomerId(Customer customer) {
        return customer.getId();
    }

    @AfterMapping
    public void updateOrder(OrderDto dto, @MappingTarget Order order) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(dto.getCustomerId()));
        order.setCustomer(customer);

        for (OrderLine orderLine: order.getOrderLines()) {
            orderLine.setOrder(order);
        }
    }
}
