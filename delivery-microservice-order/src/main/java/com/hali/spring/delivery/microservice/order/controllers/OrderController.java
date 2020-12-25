package com.hali.spring.delivery.microservice.order.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.delivery.microservice.order.services.OrderService;
import com.hali.spring.delivery.ms.model.OrderDto;
import com.hali.spring.delivery.ms.model.OrderException;

import lombok.RequiredArgsConstructor;

@RestController(value="/api/order")
@RequiredArgsConstructor
public class OrderController 
{
	private final OrderService orderService;
	
	@PostMapping("/{cartId}")
	public OrderDto createOrder(String cartId) throws OrderException
	{
		return orderService.createOrder(cartId);
	}
}

