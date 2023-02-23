package com.shoppingcart.admin.user;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.security.ShoppingUserDetails;
import com.shoppingcart.admin.security.ShoppingUserDetailsService;
import com.shoppingcart.admin.user.export.UserCsvExporter;
import com.shoppingcart.admin.user.export.UserExcelExporter;
import com.shoppingcart.admin.user.export.UserPdfExporter;

/* Controller Layer dùng để Xử Lý Yêu Cầu và truyền vào Service
 * 
 */
@Controller
public class UserController {
	@Autowired
	private UserService service;

	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";

//	@GetMapping("/users") // Phải khớp với đường dẫn <a th:href="@{/users}" trong user.html
//	public String listAll(Model model) {
//		List<User> listUserss = service.listAll();// service xử lý logic truyền xuống repo
//		model.addAttribute("listUsers", listUserss);// Model để đổ dữ liệu lên view (view là trang html) -> đổ về
//													// listUsers trong html
//		return "users/users";// trả về trang users.html
//	}

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		// @param để bắt sortField, ở đây có cặp key value là sortField và sortDir
		// Nối 2 cặp key value bằng dấu &
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Dir: " + sortDir);

		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword); // org.springframework.data.domain.Page
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {// Trg howpj endCount=21+3=24>23-->set lại endCount=23
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users/users";
	}

	@GetMapping("/users/new") // Phải khớp với đường dẫn <a th:href="@{/users/new}" trong user.html
	public String showUserInputForm(Model model) {
		List<Role> listRoles = service.listRoles();
		// Khởi tạo đối tượng rỗng user để map vào html
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/users_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			user.setPhotos(filename);
			User savedUser = service.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartFile);

		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null); // nếu ko chọn đc hình thì sẽ lưu hình là null
			service.save(user);
		}

//		System.out.println(user);
//		service.save(user);
		redirectAttributes.addFlashAttribute("messages", "The user has been saved successfully");// Key là messages ->
																									// value là The user
																									// has been saved
																									// successfully

		// return "redirect:/users";
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		try {
			User user = service.getUser(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user", user);// phải dùng "user" để map với th:object bên form
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "users/users_form";

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messages", ex.getMessage());
			// return "redirect:/users";
			// return listByPage(1, model, "firstName", "asc", null);
			return defaultRedirectURL;
		}
	}

//	@GetMapping("/account")
//	public String viewDetails(@AuthenticationPrincipal ShoppingUserDetails loggedUser, Model model)
//			throws UserNotFoundException {
//
////		User user = service.getUserByEmail(authentication.getName());
////		List<Role> listRoles = service.listRoles();
////		model.addAttribute("user", user);
////		model.addAttribute("pageTitle", "Show/Edit User (E-mail: " + user.getEmail() + ")");
////		model.addAttribute("listRoles", listRoles);
////		return "users/users_form";
//
//		String email = loggedUser.getEmail();
//		User user = service.getByEmail(email);
//		List<Role> listRoles = service.listRoles();
//		model.addAttribute("user", user);
//		model.addAttribute("pageTitle", "Show/Edit User (E-mail: " + email + ")");
//		model.addAttribute("listRoles", listRoles);
//
//		return "users/account_form";
//
//	}

//	@PostMapping("/account/update")
//	public String saveDetails(User user, RedirectAttributes redirectAttributes,
//			@AuthenticationPrincipal ShoppingUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile)
//			throws IOException {
//
//		if (!multipartFile.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			user.setPhotos(fileName);
//			User savedUser = service.updateAccount(user);
//
//			String uploadDir = "user-photos/" + savedUser.getId();
//
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//		} else {
//			if (user.getPhotos().isEmpty())
//				user.setPhotos(null);
//			service.updateAccount(user);
//		}
//
//		loggedUser.setFirstName(user.getFirstName());
//		loggedUser.setLastName(user.getLastName());
//
//		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
//
//		//return "redirect:/account";
//		return defaultRedirectURL;
//		
//
//	}

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.deleteUser(id); // xóa user

			String userPhotosDir = "user-photos/" + id;
			FileUploadUtil.removeDir(userPhotosDir); // Xóa luôn hình ảnh

			redirectAttributes.addFlashAttribute("messages", "The user id " + id + " has been deleted successfully");

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messages", ex.getMessage());

		}
		// return "redirect:/users";
		// return listByPage(1, model, "firstName", "asc", null);
		return defaultRedirectURL;
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes, Model model) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disable";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messages", message);
		// return "redirect:/users";
		// return listByPage(1, model, "firstName", "asc", null);
		return defaultRedirectURL;
	}

}
