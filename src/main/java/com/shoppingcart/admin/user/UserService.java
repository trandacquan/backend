package com.shoppingcart.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

//Tạo Spring bean userService
@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 4;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		// return (List<User>) userRepo.findAll(); //khi xuất ra file csv thì sắp xếp
		// theo firstname
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}

//	public Page<User> listByPage(int pageNum) {
//		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE);
//		return userRepo.findAll(pageable);
//	}

	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);// import org.springframework.data.domain.Sort;
		// sortField truyền vào phải đúng với thuộc tính bên user enitty vd: firstName
		// (lúc debug kiểm tra)
		// Sẽ trả về 1 đối tượng sort đã đc sắp xếp theo asc
		// Kiểm tra String sortDir nếu truyền vào là asc thì sắp xếp tăng dần, ngc lại
		// sắp xếp giảm dần
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

		if (keyword != null) {
			return userRepo.findAll(keyword, pageable);
		}

		return userRepo.findAll(pageable);
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}

		return userRepo.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public User getUser(Integer id) throws UserNotFoundException {

		try {
			return userRepo.findById(id).get();// Muốn trả về kiểu User thì phải .get()
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with id = " + id);
		}
	}
	
	public User getByEmail(String email) throws UserNotFoundException {

		try {
			return userRepo.getUserByEmail(email);
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with email = " + email);
		}
	}

	public void deleteUser(Integer id) throws UserNotFoundException {
//		try {
//			userRepo.deleteById(id);
//		} catch (NoSuchElementException ex) {
//			throw new UserNotFoundException("Could not find any user with id = " + id);
//		}
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with id = " + id);
		}
		userRepo.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);

		if (userByEmail == null)
			return true; // ko trung

		boolean isCreatingNew = (id == null);// phân biệt giữa create là null vì đâu nhập id và edit thì có giá trị

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public User updateAccount(User userInform) {
		// TODO Auto-generated method stub
		//return save(user);
		User userInDB = userRepo.findById(userInform.getId()).get();
		//userRepo.findAllById(userInForm.getId()).get();
		
		if(!userInform.getPassword().isEmpty()) {
			userInDB.setPassword(userInform.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInform.getPhotos() != null) {
			userInDB.setPhotos(userInform.getPhotos());
		}
		
		userInDB.setFirstName(userInform.getFirstName());
		userInDB.setLastName(userInform.getLastName());
		
		return userRepo.save(userInDB);
	}
	
	

}
