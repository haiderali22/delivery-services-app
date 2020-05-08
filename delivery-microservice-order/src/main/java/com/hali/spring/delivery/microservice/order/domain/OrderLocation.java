package com.hali.spring.delivery.microservice.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "order_location")
@Table(name = "order_location")
//@Embeddable
public class OrderLocation
{
	@Id
	Long id;
	
	@Embedded
	@AttributeOverrides({
	       @AttributeOverride(name="name", column = @Column(name = "manual_location_name")),
	       @AttributeOverride(name="geom", column = @Column(name = "manual_location_geom"))
	})
	private Location manualLocation;	
	
	@Embedded
	@AttributeOverrides({
	       @AttributeOverride(name="name", column = @Column(name = "system_location_name")),
	       @AttributeOverride(name="geom", column = @Column(name = "system_location_geom"))
	})
	private Location systemLocation;
	
	
}
