package com.shoppingcart.admin.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "users") // Mình sẽ thao tác trên etity để đến table, đặt table tên là user, nếu không
						// khai báo tên table hoặc entity thì c trình sẽ lấy tên class đặt cho entity và
						// table
public class User extends IdBasedEntity {
	@Column(length = 128, nullable = false, unique = true) // @column : định nghĩa tên cột
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	@Column(length = 64)
	private String photos;// để lưu tên image

	private boolean enabled;

	//lazy -> khi get user nó sẽ ko get role tương ứng của user đó
	//Eager -> khi get user nó sẽ get tất cả role của user đó
	@ManyToMany(fetch = FetchType.EAGER) // Thể hiện quan hệ nhiều nhiều
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	
//	1 user - N roles -- tuyen admin - salesperson
//	@OneToMany(mappedBy = "user123");
//	private Set<Role> children = new HashSet<>();
	
	private Set<Role> roles = new HashSet<>();

	public User() {
		super();
	}

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRoles(Role role) {
		this.roles.add(role);
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + " + email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", photos=" + photos + ", enabled=" + enabled + "]";
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Transient
	public String getPhotosImagePath() {
		if (id == null || photos == null)
			return "/images/default-user.png";
		return "/user-photos/" + this.id + "/" + this.photos;// user-photos/5/tuyen.png -> đường dẫn để lưu file hình
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();

		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

}
