package com.hali.spring.deliveryms.order.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.model.OrderException;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController 
{
	private final OrderService orderService;
	
	@PostMapping("/{cartId}")
	public OrderDto createOrder(@PathVariable("cartId")  String cartId,
		@RequestBody OrderDto order) throws OrderException
	{
		return orderService.createOrder(cartId, order);
	}
	
	@GetMapping("/{orderId}")
	public OrderDto getOrder(@PathVariable("orderId")  String orderId)
	{
		return orderService.getOrder(orderId);
	}
	
	@GetMapping("/{orderId}/state")
	public OrderState getOrderState(@PathVariable("orderId") String orderId) {
		return orderService.findCurrentStateById(orderId);
	}
}

