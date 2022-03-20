package com.learners.orderservice.repository;

import com.learners.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {

    Page<Order> findByCustomerId(UUID customerId, Pageable pageable);
}
