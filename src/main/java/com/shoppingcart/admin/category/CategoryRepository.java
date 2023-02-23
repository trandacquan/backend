package com.shoppingcart.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shoppingcart.admin.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	public long countById(Integer id);

	// Câu truy vấn findRootCategories -> truy vấn dữ liệu trong DB những đối tượng
	// mà có cha = null
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL") // .papent sẽ trả về 1 category, .id là đi vào prop của
																	// category id, trong sql so sánh với null dùng is;
																	// .parent.id is Null tức là ra cấp cao nhất (không
																	// có con)
	public List<Category> findRootCategories(Sort sort);// import org.springframework.data.domain.Sort;

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);//1 page 4 category lớn nhất

	@Query("SELECT c FROM Category c WHERE c.productName LIKE %?1%")
	public Page<Category> search(String keyword, Pageable pageable);//Có phân trang -> pagable

	@Query("SELECT c FROM Category c WHERE c.productName = :productName1")
	public Category getCategoryByName(@Param("productName1") String name);

	@Query("SELECT u FROM Category u WHERE u.productName LIKE %?1% OR u.alias LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);

	@Query("UPDATE Category u SET u.enabled = ?2 WHERE u.id = ?1") // ?1 truy đến tham số 1
	@Modifying // UPDATE/INSERT/DELETE
	public void updateEnabledStatus(Integer id, boolean enabled);
	// UPDATE Categories -> Categories chính là <Categories> ta truyền vào
	// CrudRepository

	public Category findByProductName(String name);

	public Category findByAlias(String alias);
}
