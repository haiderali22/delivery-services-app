package com.hali.spring.delivery.microservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryMicroserviceOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryMicroserviceOrderApplication.class, args);
	}

}
