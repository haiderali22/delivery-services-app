package com.hali.spring.delivery.microservice.order.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.hali.spring.delivery.microservice.order.domain.OrderState;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDTO
{
	private OrderState currentState;
	
	private Long referenceNumber;

	
//	private OrderLocation pickupLocation;
//	
//	
//	private OrderLocation devliveryLocation;
//	
//	
//	private List<ContactPerson> contacts = new ArrayList<>();	
}
