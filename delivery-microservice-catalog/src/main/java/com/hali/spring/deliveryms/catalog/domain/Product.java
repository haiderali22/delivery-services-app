package com.hali.spring.deliveryms.catalog.domain;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "products")
@Table(name = "products")
public class Product 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@PrimaryKeyJoinColumn(name  = "company_id" , foreignKey = @ForeignKey (name ="fk_product_company_id"))
	private Company company;

	@Version
	@Column(name = "version")
	private Integer version;

	@Column (name = "price")
	@NotNull
	private BigDecimal price;

	@Column (name = "product_name")
	@NotNull
	private String productName;

	@Column (name = "discription")
	private String description;

	@Column (name = "quantity")
	private int quantity;


	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "products_categories", joinColumns = {
			@JoinColumn(name = "product_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "category_id", referencedColumnName = "id") })
	private List<Category> categories;
}