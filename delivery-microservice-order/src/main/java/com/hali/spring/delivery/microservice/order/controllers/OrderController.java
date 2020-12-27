package com.hali.spring.delivery.microservice.order.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.delivery.microservice.order.services.OrderService;
import com.hali.spring.delivery.ms.model.OrderDto;
import com.hali.spring.delivery.ms.model.OrderException;

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
}

