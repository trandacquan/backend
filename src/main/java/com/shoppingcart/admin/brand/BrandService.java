package com.shoppingcart.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.category.CategoryNotFoundException;
import com.shoppingcart.admin.entity.Brand;

@Service
@Transactional
public class BrandService {

	public static final int BRANDS_PER_PAGE = 5;
	@Autowired
	private BrandRepository repo;

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public void deleteBrand(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brands with id = " + id);
		}
		repo.deleteById(id);
	}

	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll(Sort.by("name").ascending());
	}

	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);
	}

	public Brand findById(int id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
			
		}catch(NoSuchElementException ex) {
			
			throw new BrandNotFoundException("Could not find any brand");
		}
	}
	
	public List<Brand> findAllBrand() {
		return (List<Brand>) repo.findAll();
	}

}
