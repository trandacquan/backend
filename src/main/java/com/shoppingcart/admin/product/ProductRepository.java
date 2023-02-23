package com.shoppingcart.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
	public Long countById(Integer id);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
}
