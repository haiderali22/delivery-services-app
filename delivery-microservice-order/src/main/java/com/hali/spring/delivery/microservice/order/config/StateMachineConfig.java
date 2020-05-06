package com.hali.spring.delivery.microservice.order.config;

import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.state.State;

import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> 
{
	public static final String PAID_EXTENDED_STATE_VARIABLE = "paid";
	
	private final Action<OrderState, OrderEvent> paymentReceivedAction;
    private final Action<OrderState, OrderEvent> assignRiderAction;
    private final Guard<OrderState, OrderEvent> paidGuard;
    private final StateMachineListenerAdapter<OrderState, OrderEvent> listenerAdaptor;
    
//    private final StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachineRuntimePersister;    
	
	@Override
	public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception 
	{		
		states.withStates().initial(OrderState.PLACED).states(EnumSet.allOf(OrderState.class))
		.end(OrderState.CANCELED);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception
	{
		transitions
			.withExternal()
				.source(OrderState.PLACED)
				.target(OrderState.READY_FOR_DELIVERY)
				.event(OrderEvent.PAYMENT_RECEIVED)
				.action(paymentReceivedAction)
				.action(assignRiderAction)
		.and()
			.withExternal()
				.source(OrderState.PLACED)
				.target(OrderState.READY_FOR_DELIVERY)
				.event(OrderEvent.UNLOCK_DELIVERY)
				.action(assignRiderAction)
		.and()
			.withExternal()
				.source(OrderState.READY_FOR_DELIVERY)
				.target(OrderState.SENT_FOR_DELIVERY)
				.event(OrderEvent.RIDER_ASSIGNED)
		.and()
			.withExternal()
				.source(OrderState.SENT_FOR_DELIVERY)
				.target(OrderState.AWAITING_PAYMENT)
				.guard(not(paidGuard))
				.event(OrderEvent.DELIVER)
		.and()
			.withExternal()
				.source(OrderState.AWAITING_PAYMENT)
				.target(OrderState.COMPLETED)
				.event(OrderEvent.PAYMENT_RECEIVED)
				.action(paymentReceivedAction)
		.and()
			.withExternal()
				.source(OrderState.SENT_FOR_DELIVERY)
				.target(OrderState.COMPLETED)
				.event(OrderEvent.DELIVER)
				.guard(paidGuard)
		.and()
			.withExternal()
				.source(OrderState.SENT_FOR_DELIVERY)
				.target(OrderState.READY_FOR_DELIVERY)
				.event(OrderEvent.UNDELIVER)
		.and()
			.withExternal()
				.source(OrderState.PLACED)
				.target(OrderState.CANCELED)
				.event(OrderEvent.CANCEL)
		.and()
			.withExternal()
				.source(OrderState.COMPLETED)
				.event(OrderEvent.REFUND)
				.target(OrderState.PLACED)
				.guard(paidGuard)
		.and()
			.withExternal()
					.source(OrderState.PLACED)
					.target(OrderState.CANCELED)
					.event(OrderEvent.REFUND_Accepted);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {

		
		config.withConfiguration().listener(listenerAdaptor).autoStartup(false);
				//.and().withPersistence().runtimePersister(stateMachineRuntimePersister);
	}

//	private Guard<OrderState, OrderEvent> isPaid() 
//	{
//		return context -> (boolean) context.getExtendedState().get("paid", Boolean.class);
//	}

	private Guard<OrderState, OrderEvent> not(Guard<OrderState, OrderEvent> guard) {
		return context -> !guard.evaluate(context);
	}

//	private void setUnpaid(ExtendedState extendedState) {
//        log.info("Unsetting paid");
//        extendedState.getVariables().put("paid", Boolean.FALSE);
//    }
//
//	private void setPaid(ExtendedState extendedState) {
//        log.info("Setting paid");
//        extendedState.getVariables().put("paid", Boolean.TRUE);
//    }

}
