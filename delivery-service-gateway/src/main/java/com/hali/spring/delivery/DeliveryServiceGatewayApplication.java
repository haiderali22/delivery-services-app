package com.hali.spring.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DeliveryServiceGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryServiceGatewayApplication.class, args);
	}

}
