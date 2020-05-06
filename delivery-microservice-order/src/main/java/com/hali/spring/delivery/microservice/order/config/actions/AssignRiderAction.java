package com.hali.spring.delivery.microservice.order.config.actions;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.config.StateMachineConfig;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssignRiderAction implements Action<OrderState, OrderEvent> 
{
	@Override
	public void execute(StateContext<OrderState, OrderEvent> context) 
	{
		log.info("assign Rider paid");
				
	}
}
