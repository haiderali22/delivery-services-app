package com.hali.spring.deliveryms.order.config.statemachine;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderState;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentReceivedAction implements Action<OrderState, OrderEvent> 
{
	@Override
	public void execute(StateContext<OrderState, OrderEvent> context) 
	{
		log.info("Setting paid");
		ExtendedState extendedState = context.getExtendedState();
		extendedState.getVariables().put(StateMachineConfig.PAID_EXTENDED_STATE_VARIABLE, Boolean.TRUE);		
	}
}
