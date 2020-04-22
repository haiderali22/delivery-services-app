package com.hali.spring.delivery.microservice.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Order extends Auditable<Long>
{
	@Id
	private Long id;
	
	private Long referenceNumber;

	@OneToOne
	private OrderLocation pickupLocation;
	
	@OneToOne
	private OrderLocation devliveryLocation;
	
	@OneToMany
	private List<ContactPerson> contacts = new ArrayList<>();	
	
}
