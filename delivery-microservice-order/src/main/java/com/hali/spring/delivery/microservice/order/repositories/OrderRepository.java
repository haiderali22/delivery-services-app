package com.hali.spring.delivery.microservice.order.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderState;

public interface OrderRepository extends JpaRepository<Order, Long> 
{
	Page<Order> findByCurrentState(OrderState currentState, Pageable pageable);
}
