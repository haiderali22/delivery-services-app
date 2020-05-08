package com.hali.spring.delivery.microservice.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity(name = "tbl_order")
@Table(name = "tbl_order")
@Setter
@Getter
public class Order extends Auditable<Long>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id; 

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "current_state")
	private OrderState currentState;

	private Long referenceNumber;

	@OneToOne(orphanRemoval = false)
	@PrimaryKeyJoinColumn
	private OrderLocation pickupLocation;

	@OneToOne
	@PrimaryKeyJoinColumn
	private OrderLocation devliveryLocation;

	@ElementCollection
	@CollectionTable(
			name="ContactPerson",
			joinColumns = @JoinColumn(name="id")
			)
	private List<ContactPerson> contacts = new ArrayList<>();	

	@Column(name = "prepaid")
	private boolean prePaid;
}
