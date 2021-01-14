package com.hali.spring.deliveryms.order.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.order.services.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/order/cart")
@RequiredArgsConstructor
public class CartController
{
	private final CartService cartService;
	
	@GetMapping("/{cartId}")
	public List<ItemDto> getCart(@PathVariable("cartId") String cartId)
	{
		return cartService.getCart(cartId);
	}	
	
	 @PostMapping(value = "/{cartId}", params = {"productId", "quantity"})
	 public void addItemToCart(@PathVariable("cartId") String cartId , @RequestParam("productId") String productId,
	            @RequestParam("quantity") Integer quantity)
	 {
		 cartService.addItemToCart(cartId,productId,quantity);
	 }
	 
	 @DeleteMapping(value = "/{cartId}", params = {"productId"})
	 public void removeItemFromCart(@PathVariable("cartId") String cartId , 
			 @RequestParam("productId") String productId)
	 {
		 cartService.removeItemFromCart(cartId,productId);
	 } 
}
