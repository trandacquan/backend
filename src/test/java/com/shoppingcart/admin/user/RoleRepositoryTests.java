package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;

//Viết JUnit cho RoleRepository Tests
//Bản chất của units test chỉ là test không lưu database
//Rollback ban đầu là true chuyển thành false > Roll back là trả về trạng thái ban đầu trước đó
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Chạy test case với DB thật
@Rollback(false) // Mặc định sẽ rollback transaction -> Các thay đổi sẽ không được lưu khi chọn
					// true, còn false thì sẽ lưu thay đổi
public class RoleRepositoryTests {
	@Autowired // Sử dụng RoleRepository khởi tạo sẵn trong Spring IOC mà không cần khởi tạo
				// đối tượng
	private RoleRepository repo;

	// Bôi đen testCreateFirstRole nhấn chuột phải chọn Debug As > JUnitTest
	@Test // phải có annotation test thì mới chạy được JUnits
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manage everything");
		Role savedRole = repo.save(roleAdmin);

		assertThat(savedRole.getId()).isGreaterThan(0); // Kiểm tra kết quả Id trả so với 1 giá trị nào đó, ở đây là so
														// với
														// 0; sẽ trả về là true or false -> id phải lớn hơn 0 thì mới
														// đúng
	}

	@Test // phải có annotation test thì mới chạy được JUnits
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson",
				"manage product price, " + "customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "manage categories, brands, " + "products, articles and menus");
		Role roleShipper = new Role("Shipper", "view products, view orders" + "and update order status");
		Role roleAssistant = new Role("Assistant", "manage questions and reviews");
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}
