package com.shoppingcart.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Brand;


public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{
	
	public Long countById(Integer id);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword,  Pageable pageable);
	
}
