package com.hali.spring.deliveryms.model;

import java.util.List;

import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto
{
	private String currentState;
	
	private String referenceNumber;
	
	//private Point pickupLocation;
	private String pickupAddress;
	private boolean pickupAddressType;
	
	//private Point deliveryLocation;
	private String deliveryAddress;
	private boolean deliveryAddressType;

	private List<ItemDto> items;

	private String id;

	private Boolean prePaid;
}
