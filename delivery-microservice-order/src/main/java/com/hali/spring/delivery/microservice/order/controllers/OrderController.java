package com.hali.spring.delivery.microservice.order.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.delivery.microservice.order.services.OrderService;
import com.hali.spring.delivery.ms.model.OrderDTO;
import com.hali.spring.delivery.ms.model.OrderException;

import lombok.RequiredArgsConstructor;

@RestController(value="/order")
@RequiredArgsConstructor
public class OrderController 
{
	private final OrderService orderService;
	
	@PostMapping("/{cartId}")
	public OrderDTO createOrder(String cartId) throws OrderException
	{
		return orderService.createOrder(cartId);
	}
}

