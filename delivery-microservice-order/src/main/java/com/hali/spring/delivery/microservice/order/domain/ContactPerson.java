package com.hali.spring.delivery.microservice.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Embeddable()
@Setter
@Getter
public class ContactPerson 
{
	@NotEmpty
	private String number;

	@Column(name = "name")
	private 	String name;
}
