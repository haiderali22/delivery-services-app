package com.hali.spring.delivery.microservice.order.config.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IsPostPaidGuard implements Guard<OrderState, OrderEvent>  
{
	@Override
	public boolean evaluate(StateContext<OrderState, OrderEvent> context) 
	{
		return (boolean) context.getMessageHeader(OrderService.ORDER_PREPAID_HEADER);
	}

}
