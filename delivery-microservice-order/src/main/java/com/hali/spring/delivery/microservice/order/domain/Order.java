package com.hali.spring.delivery.microservice.order.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order extends Auditable<Long>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@ManyToMany (cascade = CascadeType.ALL)
	@JoinTable (name = "cart" , joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn (name = "item_id"))
	private List<Item> items;

	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn (name = "user_id")
	private User user;


	@Column (name = "ordered_date")
	@NotNull
	private LocalDate orderedDate;

	@Column (name = "total")
	private BigDecimal total;
}
