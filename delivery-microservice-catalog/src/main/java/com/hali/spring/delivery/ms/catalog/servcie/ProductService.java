package com.hali.spring.delivery.ms.catalog.servcie;

import org.hibernate.mapping.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hali.spring.delivery.ms.catalog.domain.Category;
import com.hali.spring.delivery.ms.catalog.domain.Product;
import com.hali.spring.delivery.ms.catalog.mappper.ProductMapper;
import com.hali.spring.delivery.ms.catalog.repositories.CategoryRepository;
import com.hali.spring.delivery.ms.catalog.repositories.ProductRepository;
import com.hali.spring.delivery.ms.model.ProductDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService 
{
	private final ProductRepository productRepository; 
	private final CategoryRepository categoryRepository; 
	private final ProductMapper productMapper;

	public Product getProduct(Long id) 
	{		
		return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
	}

	public Page<Product> getCatgoryProducts(Long id, Pageable pageable) {
		
		return productRepository.getCatgoryByProducts(id,pageable);
	}

	public Page<Category> getCatgories(Pageable pageable) {
		
		return categoryRepository.findAll(pageable);
	}

	public Page<Product> getCatgoryTop5Products() {
		
		return null;
	}

	public ProductDto addProduct(ProductDto product) {
		Product savedProduct = productRepository.save(productMapper.map(product));
		return productMapper.map(savedProduct);
		
	}

	public void deleteProduct(Long id) {
		 productRepository.deleteById(id);
	}
	
}
