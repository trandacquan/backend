package com.shoppingcart.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

public class ShoppingUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private User user;

	public ShoppingUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();

		List<SimpleGrantedAuthority> authories = new ArrayList<>();

		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getName()));

		}

		return authories;
	}

	@Override
	public String getPassword() {
		// user lấy từ DB lấy lên, và password đã được mã hóa
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Return True -> account ko bị locked, alse -> account bị locked, tức là để false thì bao nhiêu account đều ko login đc
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// false-> hết hạn, true-> không hết hạn
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Kiểm tra user có enalble hay ko, nếu false thì sẽ báo ko login đc do disable
		return user.isEnabled();
	}

	public String getFullname() {
		return this.user.getFirstName() + " " + this.user.getLastName();
	}
	
	public String getEmail() {
		return this.user.getEmail();
	}

	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}

	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}

}
