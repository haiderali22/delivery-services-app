package com.hali.spring.delivery.microservice.order.domain;

public enum OrderEvent 
{
	ORDER_PLACED,
	VALIDATE_ORDER,VALIDATION_PASSED,
	VALIDATE_FAILED,
	UNLOCK_DELIVERY,
    PAYMENT_RECEIVED,
    REFUND,
    DELIVER,
    CANCEL, UNDELIVER, RIDER_ASSIGNED, REFUND_Accepted, 
}
