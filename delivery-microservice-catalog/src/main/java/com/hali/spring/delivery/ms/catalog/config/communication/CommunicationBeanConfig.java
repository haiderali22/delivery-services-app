package com.hali.spring.delivery.ms.catalog.config.communication;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunicationBeanConfig
{
	public static final String ORDER_VALIDATE_QUEUE_REQUEST = "order-validate-request";
	public static final String ORDER_VALIDATE_QUEUE_RESPONSE = "order-validate-reponse";
	
	@Bean
	public NewTopic orderValidateTopic()
	{
		return new NewTopic(ORDER_VALIDATE_QUEUE_REQUEST, 1, (short) 1); 
	}	
}