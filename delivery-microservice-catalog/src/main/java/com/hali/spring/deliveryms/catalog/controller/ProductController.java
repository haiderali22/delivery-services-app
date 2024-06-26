package com.hali.spring.deliveryms.catalog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.deliveryms.catalog.domain.Category;
import com.hali.spring.deliveryms.catalog.domain.Product;
import com.hali.spring.deliveryms.catalog.servcie.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
public class ProductController
{
	private final ProductService productService;
	
	@GetMapping("/{id}")
	public Product getProduct(@PathVariable("id") String id)
	{
		return productService.getProduct(id);
	}
	
//	@GetMapping("/category")
//	public Page<Category> getCatgories(Pageable pagable)
//	{
//		return productService.getCatgories(pagable);
//	}
//	
//	@GetMapping("/category/{id}/products")
//	public Page<Product> getCatgoryProducts(@Param("id") Long id,Pageable pagable)
//	{
//		return productService.getCatgoryProducts(id,pagable);
//	}
//	
//	@GetMapping("/category/products")
//	public Page<Product> getCatgoryTop5Products()
//	{
//		return productService.getCatgoryTop5Products();
//	}
}
