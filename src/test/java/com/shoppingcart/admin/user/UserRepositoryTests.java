package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Chạy test case với DB thật
@Rollback(false) // Mặc định sẽ rollback transaction -> Các thay đổi sẽ không được lưu
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("nhbtuyen2702@gmail.com", "123456", "Nguyen Hoang Bao", "Tuyen");
		user.setEnabled(true);
		user.setPhotos("TuyenNHB.png");
		user.addRoles(roleAdmin);

		User savedUser = repo.save(user);

		assertThat(savedUser.getId()).isGreaterThan(0); // Kiểm tra kết quả trả về là true or false
	}

	@Test
	public void testCreateNewUserWithTwoRole() {
		User userRavi = new User("Tuyen1@gmail.com", "123456", "Tuyen1", "Tuyen1");

		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userRavi.addRoles(roleEditor);
		userRavi.addRoles(roleAssistant);

		User savedUser = repo.save(userRavi);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers=repo.findAll();
		for (User user : listUsers) {
			System.out.println(user);
		}
		//listUsers.forEach(user -> System.out.println(user));
	}
}
