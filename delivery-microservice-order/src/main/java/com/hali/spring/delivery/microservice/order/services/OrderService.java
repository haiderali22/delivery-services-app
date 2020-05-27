package com.hali.spring.delivery.microservice.order.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.hali.spring.delivery.microservice.order.config.statemachine.OrderStateChangeInterceptor;
import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.mapper.OrderMapper;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;
import com.hali.spring.delivery.ms.model.OrderDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService 
{
//	public static final String ORDER_ID_HEADER = "order_id";
//	public static final String ORDER_PREPAID_HEADER = "order_pre_paid";
//
//	private final OrderRepository orderRepository;
//////	private final StateMachineService<OrderState, OrderEvent> stateMachineService;
//////	private final StateMachinePersist<OrderState, OrderEvent, String> stateMachinePersist;
//////	
//////	private final StateMachineListener listener;
////	private StateMachine<OrderState, OrderEvent> currentStateMachine;
//	
//	private final StateMachineFactory<OrderState, OrderEvent> factory;
//	private final  OrderStateChangeInterceptor orderStateChangeInterceptor;
	
	private final OrderManager orderManager;
	private final OrderMapper orderMapper;

	public OrderDTO newOrder(OrderDTO orderDto) throws Exception
	{
		Order order = orderMapper.fromDTO(orderDto);
		
		order.setId(null);
		order.setCurrentState(OrderState.NEW);
		
		Order savedOrder = orderManager.placeOrder(order);
		
		
		
		return orderMapper.toDTO(savedOrder);
	}
	
	

	public void paymentReceived(Long id) throws Exception {
		
		orderManager.paymentReceived(id);
	}

	public void orderRiderAssigned(Long id) throws Exception {
		orderManager.orderRiderAssigned(id);
	}



}
