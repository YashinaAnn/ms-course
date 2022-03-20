package com.learners.orderservice.bootstrap;

import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Profile("demo")
@Component
@RequiredArgsConstructor
public class OrderDataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            loadCustomers();
        }
    }

    public void loadCustomers() {
        customerRepository.save(Customer.builder()
                .firstName("Ivan")
                .lastName("Egorov")
                .phoneNumber("+875654756457")
                .build());

        customerRepository.save(Customer.builder()
                .firstName("Petr")
                .lastName("Grigoriev")
                .phoneNumber("+875654756400")
                .build());
    }
}
