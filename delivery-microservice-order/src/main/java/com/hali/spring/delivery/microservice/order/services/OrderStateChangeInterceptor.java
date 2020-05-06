package com.hali.spring.delivery.microservice.order.services;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderState, OrderEvent> 
{
	private final OrderRepository orderRepository;
	
	@Override
	public void preStateChange(State<OrderState, OrderEvent> state, Message<OrderEvent> message,
			Transition<OrderState, OrderEvent> transition, StateMachine<OrderState, OrderEvent> stateMachine) {
		
		Optional.ofNullable(message).ifPresent( msg -> {
				Optional.ofNullable(Long.class.cast( msg.getHeaders().get(OrderService.ORDER_ID_HEADER)))
				.ifPresent( paymentID -> {
							Order order = orderRepository.getOne(paymentID);							
							order.setCurrentState(state.getId());
							
							orderRepository.save(order);
					});
		});
		
		super.preStateChange(state, message, transition, stateMachine);
	}
}