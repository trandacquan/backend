package com.shoppingcart.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//RestController sẽ trả về data thay vì trả về view như Controller
@RestController
public class CategoryRestController {
	@Autowired
	private CategoryService service;

//	@PostMapping("/categories/check_name")
//	public String checkDuplicateName(Integer id, String name) {
//		return service.isNameUnique(id, name) ? "OK" : "Duplicated";
//	}

	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
}
