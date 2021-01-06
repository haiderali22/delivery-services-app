package com.hali.spring.deliveryms.order.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.model.events.ValidateOrderReponse;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.services.OrderManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidateResponseListener 
{
	private final OrderManager orderManager;
	
	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE,
			groupId = MessagingBeanConfig.KAFKA_GROUP_ID)
	public void orderValidate(Message<ValidateOrderReponse>  response) throws Exception
	{
		orderManager.processValidationResponse(response.getPayload());
	}
}
