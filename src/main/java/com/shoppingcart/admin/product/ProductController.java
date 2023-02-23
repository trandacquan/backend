package com.shoppingcart.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.Product;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BrandService brandService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "products/products";
	}

	@GetMapping("/products/new")
	public String showProductInputForm(Model model) {
		List<Brand> listBrands = brandService.listAll();// load all brands to productcontroller, so Autowired the
														// BrandService to use the role of it.

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);//1st key -> the null products map to product html
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/products_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			product.setMainImage(filename);
			Product savedProduct = productService.save(product);

			String upDir = "products-photos/" + savedProduct.getId();

			FileUploadUtil.cleanDir(upDir);
			FileUploadUtil.saveFile(upDir, filename, multipartFile);
		} else {
			if (product.getMainImage().isEmpty())
				product.setMainImage(null);
			productService.save(product);
		}
		redirectAttributes.addFlashAttribute("messages", "The product has been saved successfully");
		return "redirect:/products";//thu github
		//thu totogit
	}

}
