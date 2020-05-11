package com.hali.spring.delivery.microservice.order.domain;

public enum OrderState 
{
	NEW,CHECK_PAYMENT_METHOD
	,PLACED,AWAITING_PAYMENT,READY_FOR_DELIVERY,SENT_FOR_DELIVERY,COMPLETED,CANCELED, REFUND;
}
