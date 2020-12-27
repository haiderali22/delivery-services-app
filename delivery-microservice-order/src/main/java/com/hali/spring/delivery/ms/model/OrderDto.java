package com.hali.spring.delivery.ms.model;

import java.util.List;

import org.springframework.data.geo.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto
{
	private String currentState;
	
	private String referenceNumber;
	
	private Point pickupLocation;
	private String pickupAddress;
	private boolean pickupAddressType;
	
	private Point deliveryLocation;
	private String deliveryAddress;
	private boolean deliveryAddressType;

	private List<ItemDto> items;

	private Long id;

	private Boolean prePaid;
}
