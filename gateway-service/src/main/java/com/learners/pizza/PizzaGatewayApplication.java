package com.learners.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PizzaGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaGatewayApplication.class, args);
	}

}
