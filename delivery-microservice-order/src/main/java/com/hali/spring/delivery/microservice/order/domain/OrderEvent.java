package com.hali.spring.delivery.microservice.order.domain;

public enum OrderEvent 
{
	UNLOCK_DELIVERY,
    PAYMENT_RECEIVED,
    REFUND,
    DELIVER,
    CANCEL, UNDELIVER, RIDER_ASSIGNED, REFUND_Accepted, 
}
