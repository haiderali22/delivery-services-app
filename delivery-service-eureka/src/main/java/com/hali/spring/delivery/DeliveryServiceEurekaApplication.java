package com.hali.spring.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DeliveryServiceEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryServiceEurekaApplication.class, args);
	}

}
