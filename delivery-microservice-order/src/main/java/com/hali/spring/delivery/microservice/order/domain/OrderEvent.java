package com.hali.spring.delivery.microservice.order.domain;

public enum OrderEvent 
{
	ORDER_PLACED,
	UNLOCK_DELIVERY,
    PAYMENT_RECEIVED,
    REFUND,
    DELIVER,
    CANCEL, UNDELIVER, RIDER_ASSIGNED, REFUND_Accepted, 
}
