package com.hali.spring.deliveryms.order.config.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IsPaidGuard implements Guard<OrderState, OrderEvent>  
{
	@Override
	public boolean evaluate(StateContext<OrderState, OrderEvent> context) 
	{
		return (boolean) context.getExtendedState().get("paid", Boolean.class);
	}

}
