package com.hali.spring.delivery.microservice.order.config;

import java.util.LinkedList;
import java.util.List;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StateMachineListener extends StateMachineListenerAdapter<OrderState, OrderEvent> 
{
	private final LinkedList<String> messages = new LinkedList<String>();

	public List<String> getMessages() {
		return messages;
	}

	public void resetMessages() {
		messages.clear();
	}

	@Override
	public void stateContext(StateContext<OrderState, OrderEvent> stateContext) {
		if (stateContext.getStage() == Stage.STATE_ENTRY) {
			messages.addFirst("Enter " + stateContext.getTarget().getId());
		} else if (stateContext.getStage() == Stage.STATE_EXIT) {
			messages.addFirst("Exit " + stateContext.getSource().getId());
		} else if (stateContext.getStage() == Stage.STATEMACHINE_START) {
			messages.addLast("Machine started");
		} else if (stateContext.getStage() == Stage.STATEMACHINE_STOP) {
			messages.addFirst("Machine stopped");
		}
	}

	@Override
	public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
		log.info(String.format("state changed from %s to %s", from.toString(),to.toString()));
	}
}
