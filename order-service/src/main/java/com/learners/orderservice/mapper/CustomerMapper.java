package com.learners.orderservice.mapper;

import com.learners.model.dto.CustomerDto;
import com.learners.orderservice.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer dtoToCustomer(CustomerDto dto);
    CustomerDto customerToDto(Customer customer);
}
