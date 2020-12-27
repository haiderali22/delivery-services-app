package com.hali.spring.delivery.microservice.order.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

	private String referenceNumber;


	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "location", column = @Column(name = "pickup_location")),
		  @AttributeOverride( name = "address", column = @Column(name = "pickup_address")),
		  @AttributeOverride( name = "systemGenerated", column = @Column(name = "pickup_address_manual"))
		})
	private Location pickupLocation;

	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "location", column = @Column(name = "delivery_location")),
		  @AttributeOverride( name = "address" , column = @Column(name = "delivery_addres" )  ),
		  @AttributeOverride( name = "systemGenerated", column = @Column(name = "delivery_address_manual"))
		})
	private Location deliveryLocation;	
	

	@Column(name = "prepaid")
	private boolean prePaid;

	@ManyToMany (cascade = CascadeType.ALL)
	@JoinTable (name = "cart" , joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn (name = "item_id"))
	private List<Item> items;

//	@ManyToOne (cascade = CascadeType.ALL) TODO: populate user
//	@JoinColumn (name = "user_id")
//	private User user;
	private String user;


	@Column (name = "ordered_date")
	@NotNull
	private LocalDate orderedDate;

	@Column (name = "total")
	private BigDecimal total;
}
