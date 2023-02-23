package com.shoppingcart.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.shoppingcart.admin.categories.export.CategoryCsvExporter;
import com.shoppingcart.admin.categories.export.CategoryExcelExporter;
import com.shoppingcart.admin.categories.export.CategoryPdfExporter;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.user.export.UserExcelExporter;
import com.shoppingcart.admin.user.export.UserPdfExporter;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

//	@GetMapping("/categories")
//	public String listAll(Model model) {
//		List<Categories> listCategories = service.listAll();
//		model.addAttribute("listCategories", listCategories);
//		return "categories/categories";
//	}

	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "productName", "asc", null);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Dir: " + sortDir);

//		Page<Category> page = service.listByPage(pageNum, sortField, sortDir, keyword);
//		List<Category> listCategories = page.getContent();

		CategoryPageInfo pageInfo = new CategoryPageInfo();//Generate pageInfo to put totalPages and totalElement info
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);

		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;

		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String showCategoriesInputForm(Model model) {
		// Khởi tạo đối tượng categories rỗng để mapping vào categories_form.html
		// Category categories = new Category();

		List<Category> listCategories = service.listCategoriesUsedInForm();

		model.addAttribute("categories", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		return "categories/categories_form";
	}

	@PostMapping("/categories/save")
	public String saveCategories(Category categories, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			categories.setPhotos(filename);
			Category savedCategories = service.save(categories);

			String upDir = "categories-photos/" + savedCategories.getId();

			FileUploadUtil.cleanDir(upDir);
			FileUploadUtil.saveFile(upDir, filename, multipartFile);
		} else {
			if (categories.getPhotos().isEmpty())
				categories.setPhotos(null);
			service.save(categories);
		}
		redirectAttributes.addFlashAttribute("messages", "The category has been saved successfully");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategories(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			Category categories = service.getCategories(id);

			model.addAttribute("categories", categories);// phải dùng "user" để map với th:object bên form
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");

			return "categories/categories_form";
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messages", ex.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategories(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.deleteCategories(id); // xóa user

			String catePhotosDir = "categories-photos/" + id;
			FileUploadUtil.removeDir(catePhotosDir); // Xóa luôn hình ảnh

			redirectAttributes.addFlashAttribute("messages", "The item id " + id + " has been deleted successfully");

		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messages", ex.getMessage());

		}
		return "redirect:/categories";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoriesEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		service.updateCategoriesEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disable";
		String message = "The item ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messages", message);
		return "redirect:/categories";
	}

	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listAll();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}

	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listAll();
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(listCategories, response);
	}

	@GetMapping("/categories/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listAll();

		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(listCategories, response);
	}

}
