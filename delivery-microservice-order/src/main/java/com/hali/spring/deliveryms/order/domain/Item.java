package com.hali.spring.deliveryms.order.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name  = "items")
@NoArgsConstructor
public class Item 
{
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column (name = "quantity")
	@NotNull
	private int quantity;

	@Column (name = "subtotal")
	@NotNull
	private BigDecimal subTotal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "products_id", unique = true)
    @MapsId
	private Product product;

	public Item(@NotNull int quantity, Product product, BigDecimal subTotal) {
		this.quantity = quantity;
		this.product = product;
		this.subTotal = subTotal;
	}

}
