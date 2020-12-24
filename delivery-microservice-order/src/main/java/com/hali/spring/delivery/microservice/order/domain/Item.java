package com.hali.spring.delivery.microservice.order.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name  = "items")
public class Item 
{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@Column (name = "quantity")
	@NotNull
	private int quantity;

	@Column (name = "subtotal")
	@NotNull
	private BigDecimal subTotal;

	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn (name = "product_id")
	private Product product;

	@ManyToMany (mappedBy = "items")
	private List<Order> orders;

	public Item(@NotNull int quantity, Product product, BigDecimal subTotal) {
		this.quantity = quantity;
		this.product = product;
		this.subTotal = subTotal;
	}

}
