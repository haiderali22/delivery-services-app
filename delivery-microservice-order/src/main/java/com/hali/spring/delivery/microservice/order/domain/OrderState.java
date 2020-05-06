package com.hali.spring.delivery.microservice.order.domain;

public enum OrderState 
{
	PLACED,AWAITING_PAYMENT,READY_FOR_DELIVERY,SENT_FOR_DELIVERY,COMPLETED,CANCELED, REFUND;
}
