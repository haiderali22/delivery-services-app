package com.hali.spring.delivery.microservice.order.controllers;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.delivery.microservice.order.services.CartService;

import lombok.RequiredArgsConstructor;

@RestController(value = "/cart")
@RequiredArgsConstructor
public class CartController
{
	private final CartService cartService;
	
	@GetMapping("/{cartId}")
	public void getCart(String cartId)
	{
		cartService.getCart(cartId);
		
	}	
	
	 @PostMapping(value = "/{cartId}", params = {"productId", "quantity"})
	 public void addItemToCart(@PathVariable("cartId") String cartId , @RequestParam("productId") Long productId,
	            @RequestParam("quantity") Integer quantity)
	 {
		 cartService.addItemToCart(cartId,productId,quantity);
	 }
	 
	 @DeleteMapping(value = "/{cartId}", params = {"productId", "quantity"})
	 public void removeItemFromCart(@PathVariable("cartId") String cartId , 
			 @RequestParam("productId") Long productId)
	 {
		 cartService.removeItemFromCart(cartId,productId);
	 }
	 
}
