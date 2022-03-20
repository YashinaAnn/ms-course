package com.learners.orderservice.repository;

import com.learners.orderservice.entity.OrderLine;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderLineRepository extends PagingAndSortingRepository<OrderLine, UUID> {
}
