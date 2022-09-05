package com.learners.pizzaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@EnableCaching
@SpringBootApplication
public class PizzaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaServiceApplication.class, args);
	}
}
