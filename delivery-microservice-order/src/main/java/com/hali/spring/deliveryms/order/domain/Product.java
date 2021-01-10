package com.hali.spring.deliveryms.order.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "products")
@Setter
@Getter
@NoArgsConstructor
public class Product
{
	    @Id
	    private String id;

	    @Column (name = "product_name")
	    @NotNull
	    
	    private String productName;
	    @Column (name = "price")
	    @NotNull
	    private BigDecimal price;

//	    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL)
//	    private List<Item> items;
}

