package com.hali.spring.deliveryms.catalog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.deliveryms.catalog.servcie.ProductService;
import com.hali.spring.deliveryms.model.ProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/product/admin")
public class AdminProductController 
{
	private final ProductService productService;
	
	@PostMapping("/")
	public ProductDto addProduct(@RequestBody ProductDto product)
	{
		return productService.addProduct(product);
	}
	
	@PutMapping("/{id}")
	public ProductDto addProduct(@PathVariable("id") String id, @RequestBody ProductDto product)
	{
		return productService.updateProduct(id,product);
	}
	
	@DeleteMapping(value = "/{id}")
    private void deleteProduct(@PathVariable("id") String id){
		productService.deleteProduct(id);
	}
}
