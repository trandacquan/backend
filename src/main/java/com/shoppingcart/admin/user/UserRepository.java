package com.shoppingcart.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shoppingcart.admin.entity.User;

//Sring data jpa -> để định ngĩa tạo 1 class
//Model: tạo 1 class bình thường -> ko có anotation nào để tạo table
//Entity: có table tương ứng trong database.
//CrudRepository truyền vào tham số đầu là entity, thứ 2 là kiểu của khóa chính (phải truyền wrapper class)
//Dùng UserRepository để thao tác với DB
//Khi kế thừa thì sử dụng toàn bộ phương thức trong đó

//public interface UserRepository extends CrudRepository<User, Integer> {
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.email = :email1")
	public User getUserByEmail(@Param("email1") String email);//Truyền giá trị @Param("email1") vào sau dấu :

//	@Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%")
//	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' '," + " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	public long countById(Integer id);// JPA convention

	// Truy vấn trên entity và thuộc tính
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1") //?1 truy đến tham số 1
	@Modifying // UPDATE/INSERT/DELETE
	public void updateEnabledStatus(Integer id, boolean enabled);

}
