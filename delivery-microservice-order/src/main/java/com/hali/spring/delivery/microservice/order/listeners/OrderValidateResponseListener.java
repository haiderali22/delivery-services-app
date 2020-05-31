package com.hali.spring.delivery.microservice.order.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.config.communication.CommunicationBeanConfig;
import com.hali.spring.delivery.microservice.order.services.OrderManager;
import com.hali.spring.delivery.ms.model.events.OrderValidateResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidateResponseListener 
{
	private final OrderManager orderManager;
	
	@KafkaListener(topics = CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE,
			groupId = "order-delivery")
	public void orderValidate(Message<OrderValidateResponse>   response) throws Exception
	{
		orderManager.processValidationResponse(response.getPayload());
	}
}
