package com.hali.spring.delivery.microservice.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.geo.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@Entity(name = "order_location")
//@Table(name = "order_location")
@Embeddable
public class Location
{
	private Point location;	
	private String address;
	private boolean systemGenerated;
}
