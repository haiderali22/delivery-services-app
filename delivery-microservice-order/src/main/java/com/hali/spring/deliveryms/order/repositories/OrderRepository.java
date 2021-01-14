package com.hali.spring.deliveryms.order.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderState;

public interface OrderRepository extends JpaRepository<Order, String> 
{
	Page<Order> findByCurrentState(OrderState currentState, Pageable pageable);
	
	 @Query("SELECT t.currentState FROM Order t where t.id = :id")	 
	 OrderState findCurrentStateById(@Param("id") String id);
}
