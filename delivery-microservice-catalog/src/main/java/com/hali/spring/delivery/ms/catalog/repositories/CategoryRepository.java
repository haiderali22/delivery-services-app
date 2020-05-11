package com.hali.spring.delivery.ms.catalog.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hali.spring.delivery.ms.catalog.domain.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> 
{
	
}