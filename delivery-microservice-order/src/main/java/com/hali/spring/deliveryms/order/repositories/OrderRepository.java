package com.hali.spring.deliveryms.order.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderState;

public interface OrderRepository extends JpaRepository<Order, String> 
{
	Page<Order> findByCurrentState(OrderState currentState, Pageable pageable);
}
