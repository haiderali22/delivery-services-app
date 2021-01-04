package com.hali.spring.deliveryms.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hali.spring.deliveryms.order.domain.OrderHistory;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> 
{
	@Query("FROM order_history o where order_id = :orderID")
	List<OrderHistory> getAllByOrder(@Param("orderID") Long orderID);

}
