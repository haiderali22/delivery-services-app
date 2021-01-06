package com.hali.spring.deliveryms.order.config.statemachine;

import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.deliveryms.model.events.ValidateOrderRequest;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderEvent;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.mapper.OrderMapper;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;
import com.hali.spring.deliveryms.order.services.OrderManager;
import com.hali.spring.deliveryms.order.utils.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateOrderAction implements Action<OrderState, OrderEvent> 
{
	private final KafkaTemplate<String,Object> template;
	private final OrderRepository orderRepository;
	private final OrderMapper mapper;
	private final ObjectMapper objMapper;

	@Override
	public void execute(StateContext<OrderState, OrderEvent> context) {
		Optional.ofNullable(context.getMessage()).ifPresent( msg -> {
			Optional.ofNullable(Long.class.cast( msg.getHeaders().get(OrderManager.ORDER_ID_HEADER)))
			.ifPresent( orderId -> {
				Optional<Order> order =  orderRepository.findById(orderId);

				if(order.isPresent()){
					ValidateOrderRequest req = ValidateOrderRequest.builder().
							order(mapper.map(order.get())).build();
					
						template.send(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST,
														JsonUtil.convertToString(objMapper, req));
					//MessageBuilder.withPayload(req).build());
				}
				else{
					log.error("Order Not Found. Id: " + orderId);
				}

			});
		});

		//		context.getStateMachine().sendEvent(OrderEvent.UNLOCK_DELIVERY);

	}
}
