package com.hali.spring.delivery.microservice.order.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hali.spring.delivery.microservice.order.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController 
{
	private final OrderService orderService;
	
	
}
