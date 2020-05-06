package com.hali.spring.delivery.microservice.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.springframework.data.geo.Point;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class Location 
{
	private String name;
	private Point geom;	
}
