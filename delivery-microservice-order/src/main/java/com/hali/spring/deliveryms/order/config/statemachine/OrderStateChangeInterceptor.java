package com.hali.spring.deliveryms.order.config.statemachine;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderHistory;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.repositories.OrderHistoryRepository;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;
import com.hali.spring.deliveryms.order.services.OrderManager;
import com.hali.spring.deliveryms.order.services.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderState, OrderEvent> 
{
	private final OrderRepository orderRepository;
//	private final OrderHistoryRepository orderHistoryRepository;
	
	@Override
	@Transactional
	public void preStateChange(State<OrderState, OrderEvent> state, Message<OrderEvent> message,
			Transition<OrderState, OrderEvent> transition, StateMachine<OrderState, OrderEvent> stateMachine) {
		
		Optional.ofNullable(message).ifPresent( msg -> {
				Optional.ofNullable(String.class.cast( msg.getHeaders().get(OrderManager.ORDER_ID_HEADER)))
				.ifPresent( orderID -> {
							Order order = orderRepository.getOne(orderID);	
							
							OrderState previousState = order.getCurrentState();
							
							order.setCurrentState(state.getId());
							
							orderRepository.saveAndFlush(order);
							
//							OrderHistory oHistory = new OrderHistory();
//							
//							oHistory.setCurrentState(state.getId());
//							oHistory.setPreviousState(previousState);
//							oHistory.setEvent(transition.getTrigger().getEvent());
//							
//							oHistory.setOrder(order);
//							
//							orderHistoryRepository.save(oHistory);
							
					});
		});
		
	}
}