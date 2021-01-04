package com.hali.spring.deliveryms.order.config.statemachine;

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

import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> 
{
	public static final String PAID_EXTENDED_STATE_VARIABLE = "paid";
	
	private final Action<OrderState, OrderEvent> validateOrderAction;
	private final Action<OrderState, OrderEvent> paymentReceivedAction;
    private final Action<OrderState, OrderEvent> assignRiderAction;
    private final Action<OrderState, OrderEvent> orderPlacedAction;
    private final Guard<OrderState, OrderEvent> isPaidGuard;
    private final Guard<OrderState, OrderEvent> isPostPaidGuard;
    private final StateMachineListenerAdapter<OrderState, OrderEvent> listenerAdaptor;
    
//    private final StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachineRuntimePersister;    
	
	@Override
	public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception 
	{		
		states.withStates()		
		.initial(OrderState.NEW).states(EnumSet.allOf(OrderState.class))
		.end(OrderState.CANCELED)
		.stateEntry(OrderState.PLACED, orderPlacedAction);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception
	{
		transitions
			.withExternal()
				.source(OrderState.NEW)
				.event(OrderEvent.VALIDATE_ORDER)
				.target(OrderState.VALIDATION_PENDING)
				.action(validateOrderAction)
		.and()
			.withExternal()
				.source(OrderState.VALIDATION_PENDING)
				.event(OrderEvent.VALIDATION_PASSED)
				.target(OrderState.PLACED)
		.and()
			.withExternal()
				.source(OrderState.VALIDATION_PENDING)
				.event(OrderEvent.VALIDATE_FAILED)
				.target(OrderState.VALIDATION_FAILED)
		.and()
			.withExternal()
				.source(OrderState.PLACED)
				.event(OrderEvent.ORDER_PLACED)
				.target(OrderState.AWAITING_PAYMENT)
		.and()
			.withExternal()
				.source(OrderState.AWAITING_PAYMENT)
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
				.guard(not(isPaidGuard))
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
				.guard(isPaidGuard)
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
				.guard(isPaidGuard)
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
