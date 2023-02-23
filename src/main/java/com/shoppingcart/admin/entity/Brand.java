package com.shoppingcart.admin.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand extends IdBasedEntity {
	@Column(length = 45, nullable = false, unique = true)
	private String name;

	@Column(length = 128)
	private String logo;

	// Generate a brands_categories table which is has 2 columns: brand_id,
	// category_id
	//
	@ManyToMany(fetch = FetchType.EAGER) // Thể hiện quan hệ nhiều nhiều
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Brand() {
		super();
	}

	public Brand(String name, String logo) {
		super();
		this.name = name;
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Transient
	public String getLogoPath() {
		if (id == null || logo == null) {
			return "/images/default-user.png";
		}
		return "/brands-photos/" + this.id + "/" + this.logo;
	}

}
