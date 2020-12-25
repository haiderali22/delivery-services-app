package com.hali.spring.delivery.microservice.order.config.statemachine;

import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.delivery.microservice.order.config.communication.CommunicationBeanConfig;
import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.mapper.OrderMapper;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;
import com.hali.spring.delivery.microservice.order.services.OrderManager;
import com.hali.spring.delivery.ms.model.events.OrderValidationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateOrderAction implements Action<OrderState, OrderEvent> 
{
	private final KafkaTemplate<String,String> template;
	private final OrderRepository orderRepository;
	private final OrderMapper mapper;
	private final ObjectMapper objMapper;

	@Override
	public void execute(StateContext<OrderState, OrderEvent> context) 
	{

		Optional.ofNullable(context.getMessage()).ifPresent( msg -> {
			Optional.ofNullable(Long.class.cast( msg.getHeaders().get(OrderManager.ORDER_ID_HEADER)))
			.ifPresent( orderId -> {

				Optional<Order> order =  orderRepository.findById(orderId);

				if(order.isPresent())
				{
					OrderValidationRequest req = OrderValidationRequest.builder().
							order(mapper.map(order.get())).build();
					try {
						template.send(CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST,
								objMapper.writeValueAsString(req));
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//							MessageBuilder.withPayload(req).build());
				}
				else
				{
					log.error("Order Not Found. Id: " + orderId);
				}

			});
		});

		//		context.getStateMachine().sendEvent(OrderEvent.UNLOCK_DELIVERY);

	}
}
