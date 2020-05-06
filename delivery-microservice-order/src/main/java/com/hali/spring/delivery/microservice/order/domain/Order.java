package com.hali.spring.delivery.microservice.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.AbstractAuditable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order")
@Setter
@Getter
public class Order //extends AbstractAuditable<Long,Long>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id; 
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "current_state")
	private OrderState currentState;
	
//	private Long referenceNumber;

//	@OneToOne(orphanRemoval = false)
//	@PrimaryKeyJoinColumn
//	private OrderLocation pickupLocation;
//	
//	@OneToOne
//	private OrderLocation devliveryLocation;
//	
//	@OneToMany
//	private List<ContactPerson> contacts = new ArrayList<>();	
}
