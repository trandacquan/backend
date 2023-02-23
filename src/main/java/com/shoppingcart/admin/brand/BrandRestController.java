package com.shoppingcart.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@RestController
public class BrandRestController {
	@Autowired
	private BrandService brandService;

	@GetMapping("/brands/{brandId}/categories")
	public List<CategoryDTO> getListCategoriesByBrand(@PathVariable(name = "brandId") Integer brandId)
			throws BrandNotFoundRestException {

		List<CategoryDTO> listCategories = new ArrayList<>();

		try {
			Brand brand = brandService.findById(brandId);
			Set<Category> categories = brand.getCategories();

			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getProductName());
				listCategories.add(dto);
			}

			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}

	}
}
//MY CODE -> FIRSTLY DO IN PRODUCT REST CONTROLLER
//@RestController
//public class ProductRestController {
//	@Autowired
//	private BrandService brandService;
//
//	@GetMapping("/brands/{brandId}/categories")
//	public List<Category> getListCategoriesByBrand(@PathVariable(name = "brandId") int id)
//			throws BrandNotFoundException {
//		Brand brand = new Brand();
//		brand = brandService.findById(id);
//
//		List<Category> newListCategoriesByBrand=new ArrayList<>();
//		Set<Category> listCategoriesByBrand = brand.getCategories();
//
//		for (Category categoryByBrand : listCategoriesByBrand) {
//			newListCategoriesByBrand.add(Category.copyIdAndName(categoryByBrand));
//		}
//
//		return newListCategoriesByBrand;
//	}
//
//}
