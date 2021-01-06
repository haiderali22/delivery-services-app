package com.hali.spring.deliveryms.order.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hali.spring.deliveryms.model.events.OrderPaymentResponse;
import com.hali.spring.deliveryms.model.events.ValidateOrderReponse;
import com.hali.spring.deliveryms.order.config.statemachine.OrderStateChangeInterceptor;
import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;

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
	private final OrderStateChangeInterceptor orderStateChangeInterceptor;

	@Transactional
	public Order placeOrder(Order order)
	{
		Order savedOrder = orderRepository.saveAndFlush(order);

		//sendEvent(savedOrder.getId(),OrderEvent.ORDER_PLACED);
		//
		//		Message<OrderEvent> msg = MessageBuilder.withPayload(OrderEvent.VALIDATE_ORDER).
		//				setHeader(ORDER_ID_HEADER, savedOrder.getId()).
		//				setHeader(ORDER_PREPAID_HEADER, savedOrder.isPrePaid())
		//				.build();

		sendEvent(savedOrder,OrderEvent.VALIDATE_ORDER);

		return savedOrder;
	}

	private void sendEvent(Order order , 
			OrderEvent event) 
	{
		Message<OrderEvent> msg = MessageBuilder.withPayload(event).
				setHeader(ORDER_ID_HEADER,  order.getId()).
				setHeader(ORDER_PREPAID_HEADER, order.isPrePaid())
				.build();

		getStateMachine( order.getId()).sendEvent(msg);
	}

	private void sendEvent(Long orderId , 
			OrderEvent event)
	{
		Message<OrderEvent> msg = MessageBuilder.withPayload(event).
				setHeader(ORDER_ID_HEADER,  orderId)
				.build();

		getStateMachine(orderId).sendEvent(msg);
	}

	public void paymentReceived(Long id )
	{
		sendEvent(id,OrderEvent.PAYMENT_RECEIVED);
	}

	public void orderRiderAssigned(Long id) 
	{
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

	@Transactional
	public void processValidationResponse(ValidateOrderReponse response) throws Exception 
	{
		Order order = orderRepository.getOne(response.getOrderId());

		if(response.isValidated())
			sendEvent(order,OrderEvent.VALIDATION_PASSED);
		else
			sendEvent(order.getId(),OrderEvent.VALIDATE_FAILED);

	}

	@Transactional
	public void processPaymentResponse(OrderPaymentResponse response) 
	{
		Order order = orderRepository.getOne(response.getOrderId());

		if(response.isRecevied())
			sendEvent(order,OrderEvent.PAYMENT_RECEIVED);
		else
			sendEvent(order.getId(),OrderEvent.CANCEL);
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
