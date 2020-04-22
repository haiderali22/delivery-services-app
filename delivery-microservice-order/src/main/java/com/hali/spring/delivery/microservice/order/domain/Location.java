package com.hali.spring.delivery.microservice.order.domain;

import javax.persistence.Id;

import org.springframework.data.geo.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location 
{
	@Id
	private Long id;
	private String name;
	private Point location;
	private LocationType type;
	
}
