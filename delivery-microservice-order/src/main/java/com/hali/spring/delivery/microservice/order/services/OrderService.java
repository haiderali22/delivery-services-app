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

import com.hali.spring.delivery.microservice.order.config.StateMachineListener;
import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService 
{
	public static final String ORDER_ID_HEADER = "order_id";

	private final OrderRepository orderRepository;
////	private final StateMachineService<OrderState, OrderEvent> stateMachineService;
////	private final StateMachinePersist<OrderState, OrderEvent, String> stateMachinePersist;
////	
////	private final StateMachineListener listener;
//	private StateMachine<OrderState, OrderEvent> currentStateMachine;
	
	private final StateMachineFactory<OrderState, OrderEvent> factory;
	private final  OrderStateChangeInterceptor orderStateChangeInterceptor;

	public Order newOrder(Order order) throws Exception
	{
		Order savedOrder = orderRepository.save(order);
		
		sendEvent(savedOrder.getId(),OrderEvent.UNLOCK_DELIVERY);
		
		return savedOrder;
	}
	
	private void sendEvent(Long orderId, OrderEvent event) throws Exception 
	{	
		
		Message<OrderEvent> msg = MessageBuilder.withPayload(event).
					setHeader(ORDER_ID_HEADER, orderId).build();
		
		getStateMachine(orderId).sendEvent(msg);
	}
	
	private synchronized StateMachine<OrderState, OrderEvent>  getStateMachine(Long orderId)
	{
		Order order = orderRepository.getOne(orderId);

		StateMachine<OrderState, OrderEvent> sm = factory.getStateMachine(Long.toString(orderId));
		
		sm.stop();
		
		sm.getStateMachineAccessor().doWithAllRegions( (sma) -> {
			
			sma.addStateMachineInterceptor(orderStateChangeInterceptor);
			sma.resetStateMachine(new DefaultStateMachineContext<OrderState, OrderEvent>(order.getCurrentState(), 
									null, null, null));	
		} );
		
		sm.start();
		
		return sm;
	}

	public void paymentReceived(Long id) throws Exception {
		
		sendEvent(id,OrderEvent.PAYMENT_RECEIVED);
	}

	public void orderRiderAssigned(Long id) throws Exception {
		sendEvent(id,OrderEvent.RIDER_ASSIGNED);
	}


//	private synchronized StateMachine<OrderState, OrderEvent> getStateMachine(Long orderId) throws Exception 
//	{
//		String machineId = Long.toString(orderId);
//		
//		listener.resetMessages();
//		if (currentStateMachine == null) {
//			currentStateMachine = stateMachineService.acquireStateMachine(machineId);
//			currentStateMachine.addStateListener(listener);
//			currentStateMachine.start();
//		} else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
//			stateMachineService.releaseStateMachine(currentStateMachine.getId());
//			currentStateMachine.stop();
//			currentStateMachine = stateMachineService.acquireStateMachine(machineId);
//			currentStateMachine.addStateListener(listener);
//			currentStateMachine.start();
//		}
//		return currentStateMachine;
//	}
}
