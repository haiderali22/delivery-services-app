package com.hali.spring.deliveryms.order.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

//@Entity(name = "order_history")
//@Table(name = "order_history")
//@EntityListeners(AuditingEntityListener.class)
//@Setter
//@Getter
public class OrderHistory 
{
	@Id
    @GeneratedValue
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_history_id"))
	Order order;

	@CreatedBy
	private Long modifiedBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Enumerated(EnumType.STRING)
	private OrderEvent event;

	@Enumerated(EnumType.STRING)
	private OrderState previousState;
	
	@Enumerated(EnumType.STRING)
	private OrderState currentState;
}
