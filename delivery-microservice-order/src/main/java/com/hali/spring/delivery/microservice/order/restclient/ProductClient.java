package com.hali.spring.delivery.microservice.order.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hali.spring.delivery.ms.model.ProductDto;

@FeignClient(name = "product-catalog-service")
public interface ProductClient {
	@GetMapping(value = "/api/product/{id}")
	public ProductDto getProductById(@PathVariable(value = "id") Long productId);
}
