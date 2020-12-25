package com.hali.spring.delivery.ms.model;

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
public class OrderDto
{
	private String currentState;
	
	private Long referenceNumber;

	private List<ItemDto> orderLines;

	private Long id;

	private Boolean prePaid;
}
