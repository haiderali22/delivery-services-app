package com.hali.spring.deliveryms.catalog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.deliveryms.catalog.model.ProductDto;
import com.hali.spring.deliveryms.catalog.servcie.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminProductController 
{
	private final ProductService productService;
	
	@PostMapping("/product")
	public ProductDto addProduct(@RequestBody ProductDto product)
	{
		return productService.addProduct(product);
	}
	
	@DeleteMapping(value = "/products/{id}")
    private void deleteProduct(@PathVariable("id") Long id){
		productService.deleteProduct(id);
	}
}
