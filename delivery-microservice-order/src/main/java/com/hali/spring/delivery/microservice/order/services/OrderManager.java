package com.hali.spring.delivery.microservice.order.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.hali.spring.delivery.microservice.order.config.statemachine.OrderStateChangeInterceptor;
import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderManager 
{
	public static final String ORDER_ID_HEADER = "order_id";
	public static final String ORDER_PREPAID_HEADER = "order_pre_paid";

	private final OrderRepository orderRepository;
	private final StateMachineFactory<OrderState, OrderEvent> factory;
	private final  OrderStateChangeInterceptor orderStateChangeInterceptor;

	public Order placeOrder(Order order) 
	{
		Order savedOrder = orderRepository.save(order);

		//sendEvent(savedOrder.getId(),OrderEvent.ORDER_PLACED);

		Message<OrderEvent> msg = MessageBuilder.withPayload(OrderEvent.ORDER_PLACED).
				setHeader(ORDER_ID_HEADER, savedOrder.getId()).
				setHeader(ORDER_PREPAID_HEADER, savedOrder.isPrePaid())
				.build();

		getStateMachine(savedOrder.getId()).sendEvent(msg);
		return savedOrder;
	}

	private void sendEvent(Long orderId, OrderEvent event) throws Exception 
	{	

		Message<OrderEvent> msg = MessageBuilder.withPayload(event).
				setHeader(ORDER_ID_HEADER, orderId)
				.build();

		getStateMachine(orderId).sendEvent(msg);
	}

	public void paymentReceived(Long id) throws Exception {

		sendEvent(id,OrderEvent.PAYMENT_RECEIVED);
	}

	public void orderRiderAssigned(Long id) throws Exception {
		sendEvent(id,OrderEvent.RIDER_ASSIGNED);
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
		});

		sm.start();

		return sm;
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
