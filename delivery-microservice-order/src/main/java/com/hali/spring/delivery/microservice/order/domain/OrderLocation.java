package com.hali.spring.delivery.microservice.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderLocation
{
	@Id
	private Long id;
	
	@OneToOne
	private Location manualLocation;
	
	@OneToOne
	private Location systemLocation;
	
	
}
