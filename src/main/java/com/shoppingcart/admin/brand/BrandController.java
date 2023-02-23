package com.shoppingcart.admin.brand;

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
import com.shoppingcart.admin.category.CategoryPageInfo;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;
	
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";

//	@GetMapping("/brands")
//	public String listAll(Model model) {
//		List<Brand> listBrandsToView = brandService.listAll();
//		model.addAttribute("listBrands", listBrandsToView);
//
//		return "brands/brands";
//	}
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String showBrandInputForm(Model model) {
		Brand brand = new Brand();
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("brand", brand);// keyvalue 'brand' in html
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brands_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			brand.setLogo(filename);
			Brand savedBrand = brandService.save(brand);

			String upDir = "brands-photos/" + savedBrand.getId();

			FileUploadUtil.cleanDir(upDir);
			FileUploadUtil.saveFile(upDir, filename, multipartFile);
		} else {
			if (brand.getLogo().isEmpty())
				brand.setLogo(null);
			brandService.save(brand);
		}
		redirectAttributes.addFlashAttribute("messages", "The brand has been saved successfully");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name ="id")Integer id, Model model,Brand brand, RedirectAttributes redirectAttributes) {
		try {
			brand = brandService.findById(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategories",listCategories);
			model.addAttribute("brand",brand);
			model.addAttribute("pageTittle","Edit Brand");
			return "brands/brands_form";
		}catch(BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return defaultRedirectURL;
		}
	
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttribute) {
		try {
			brandService.deleteBrand(id);
			String brandLogo = "logo-photos/" + id;
			FileUploadUtil.removeDir(brandLogo);
			redirectAttribute.addFlashAttribute("message","The user ID: " + id +" has been deleted");
		}catch(BrandNotFoundException ex) {
			redirectAttribute.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

}
