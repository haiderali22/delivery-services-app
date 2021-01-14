package com.hali.spring.deliveryms.order.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
public class Order 
//extends Auditable<Long>
{
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id")
	private String id; 

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "current_state")
	private OrderState currentState;

	private String referenceNumber;


	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "location", column = @Column(name = "pickup_location",columnDefinition = "POINT")),
		  @AttributeOverride( name = "address", column = @Column(name = "pickup_address")),
		  @AttributeOverride( name = "systemGenerated", column = @Column(name = "pickup_address_manual"))
		})
	private Location pickupLocation;

	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "location", column = @Column(name = "delivery_location",columnDefinition = "POINT")),
		  @AttributeOverride( name = "address" , column = @Column(name = "delivery_addres" )  ),
		  @AttributeOverride( name = "systemGenerated", column = @Column(name = "delivery_address_manual"))
		})
	private Location deliveryLocation;	
	

	@Column(name = "prepaid")
	private boolean prePaid;

	@OneToMany (cascade = CascadeType.ALL)
	private List<Item> items = new ArrayList<>();

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
