package com.hali.spring.deliveryms.identityprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class DeliveryServiceIdentityproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryServiceIdentityproviderApplication.class, args);
	}

}
