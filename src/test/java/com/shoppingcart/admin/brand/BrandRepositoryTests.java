package com.shoppingcart.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Brand;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Chạy test case với DB thật
@Rollback(false) // Mặc định sẽ rollback transaction -> Các thay đổi sẽ không được lưu
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewBrand() {
		Brand brand = new Brand("Acer", "acer.png");
		
		Brand savedBrand=repo.save(brand);
		
		assertThat(savedBrand.getId()).isGreaterThan(0); // Kiểm tra kết quả trả về là true or false
	}
}
