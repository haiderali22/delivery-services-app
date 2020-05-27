package com.hali.spring.delivery.ms.catalog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "item")
public class Item
{
	@Column(name = "product_id")
	private Long id;
	
	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private Long price;
}
