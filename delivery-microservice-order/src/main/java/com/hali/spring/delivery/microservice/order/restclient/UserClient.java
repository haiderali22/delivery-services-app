package com.hali.spring.delivery.microservice.order.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hali.spring.delivery.microservice.order.domain.User;

@FeignClient(name = "User", url = "http://localhost:8811/")
public interface UserClient {

	@GetMapping(value = "/users/{id}")
	public User getUserById(@PathVariable("id") String id);
}