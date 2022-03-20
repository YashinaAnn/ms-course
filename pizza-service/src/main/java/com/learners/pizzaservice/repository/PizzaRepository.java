package com.learners.pizzaservice.repository;

import com.learners.pizzaservice.entity.Pizza;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaRepository extends PagingAndSortingRepository<Pizza, Long> {

    @Modifying
    @Query(value = "DELETE FROM pizza WHERE id = :id", nativeQuery = true)
    boolean deletePizzaById(Long id);

    Optional<Pizza> findByUpc(String upc);
}
