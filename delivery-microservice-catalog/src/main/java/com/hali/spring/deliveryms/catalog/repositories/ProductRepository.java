package com.hali.spring.deliveryms.catalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.hali.spring.deliveryms.catalog.domain.Product;

public interface ProductRepository  extends PagingAndSortingRepository<Product,String>
{
	@Query("FROM Product p where p.category_id = :category_id")
	Page<Product> getCatgoryByProducts(@Param("category_id") Long id, Pageable pageable);	
	
	boolean existsById(String id);
}
