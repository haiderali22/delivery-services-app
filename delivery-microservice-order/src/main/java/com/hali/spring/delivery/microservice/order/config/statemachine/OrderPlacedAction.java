package com.hali.spring.delivery.microservice.order.config.statemachine;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderHistory;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.services.OrderManager;
import com.hali.spring.delivery.microservice.order.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderPlacedAction implements Action<OrderState, OrderEvent> 
{
	@Override
	public void execute(StateContext<OrderState, OrderEvent> context) 
	{

		Optional.ofNullable(context.getMessage()).ifPresent( msg -> {
			Optional.ofNullable(Boolean.class.cast( msg.getHeaders().get(OrderManager.ORDER_PREPAID_HEADER)))
			.ifPresent( isPrePaid -> {
					
				if(!isPrePaid)
				{
					Message<OrderEvent> omsg = MessageBuilder.withPayload(OrderEvent.UNLOCK_DELIVERY).
							setHeader(OrderManager.ORDER_ID_HEADER, 
									Long.class.cast( msg.getHeaders().get(OrderManager.ORDER_ID_HEADER))).
							setHeader(OrderManager.ORDER_PREPAID_HEADER, isPrePaid)
							.build();
					
					context.getStateMachine().sendEvent(omsg);
				}

			});
		});
		
//		context.getStateMachine().sendEvent(OrderEvent.UNLOCK_DELIVERY);
		
	}
}
