package com.hali.spring.deliveryms.order.domain;

import javax.persistence.Embeddable;

import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@Entity(name = "order_location")
//@Table(name = "order_location")
@Embeddable
public class Location
{
	//private Point location;	
	private String address;
	private boolean systemGenerated;
}
