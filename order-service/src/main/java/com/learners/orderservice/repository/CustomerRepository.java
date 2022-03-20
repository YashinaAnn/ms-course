package com.learners.orderservice.repository;

import com.learners.orderservice.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID> {
}
