package com.shoppingcart.admin.categories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.category.CategoryRepository;
import com.shoppingcart.admin.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoriesRepositoryTests {
	@Autowired
	private CategoryRepository cateRepo;

	@Autowired
	private TestEntityManager entityManager;

//	@Test
//	public void testCreateCategories() {
//		Categories newItem = new Categories("hp5", "h5", true);
//		Categories savedItem = cateRepo.save(newItem);
//		assertThat(savedItem.getId()).isGreaterThan(0);
//	}

	@Test
	public void testCreateRootCategories() {
		Category computers = new Category("Computers");// id=1
		// cateRepo.save(computers);

		Category desktops = new Category("Desktops", computers);

		Category laptops = new Category("Laptops", computers);
		Category laptop1 = new Category("Laptop 1", laptops);
		Category laptop2 = new Category("Laptop 2", laptops);
		Category laptop21 = new Category("Laptop 21", laptop2);
		Category laptop211 = new Category("Laptop 211", laptop21);
		Category laptop3 = new Category("Laptop 3", laptops);

		Category computerComponents = new Category("Computer components", computers);
		Category memoryA = new Category("Memory a", computerComponents);
		Category a1 = new Category("a1", memoryA);
		Category a2 = new Category("a2", a1);
		Category a3 = new Category("a3", a2);
		Category a4 = new Category("a4", a3);
		Category memoryB = new Category("Memory b", computerComponents);
		Category b1 = new Category("b1", memoryB);
		Category b2 = new Category("b2", b1);

		cateRepo.saveAll(List.of(computers, desktops, laptops, laptop1, laptop2, laptop21, computerComponents, memoryA,
				a1, a2, a3, a4, memoryB, b1, b2));

//		Categories electronics = new Categories("Electronics");//id=1
//		cateRepo.save(electronics);
//		
//		Categories desktops = new Categories("Desktops", computers);//id=1
//		cateRepo.save(desktops);
//		
//		Categories computerComponents = new Categories("Computer component");//id=1
//		cateRepo.save(computerComponents);
//		
//		Categories memory = new Categories("Memory", computerComponents);//id=1
//		cateRepo.save(memory);
//		
//		Categories cameras = new Categories("Cameras", electronics);//id=1
//		cateRepo.save(cameras);
//		
//		Categories smartphones = new Categories("Smartphones", electronics);//id=1
//		cateRepo.save(smartphones);

	}

	@Test
	public void testParentCallsChildren() {
		Category computers = entityManager.find(Category.class, 1);
		System.out.println(computers.getProductName());
		testPrintChildren(computers.getChildren(), "--");
	}

	public void testPrintChildren(Set<Category> listCategory, String underline) {
		for (Category category : listCategory) {
			System.out.print(underline);
			System.out.println(category.getProductName());

			if (category.getChildren() != null) {
				testPrintChildren(category.getChildren(), underline + "--");
			}

		}
	}

}
