package com.shoppingcart.admin.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

/*Entity -> Dùng để thao tác với DB; khi có annotation @Entity, chạy chương trình sẽ tạo ra Table dưới DB
Khi không khai báo tên table thì chương trình sẽ dùng tên class để làm tên table
trong jpa thì truy vấn thông qua thuộc tính*/

@Entity
@Table(name = "categories")

public class Category extends IdBasedEntity {

	@Column(length = 40, nullable = false, unique = true)
	private String productName;

	@Column(length = 40, nullable = false)
	private String alias;

	@Column(length = 64)
	private String photos;

	private boolean enabled;

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Transient // Sẽ không ánh xạ thuộc tính nào vào table
	private boolean hasChildren;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent") // anh xa toi chinh no
	@OrderBy("product_name asc")
	private Set<Category> children = new HashSet<>();

	public Category() {
		super();
	}

	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setProductName(category.getProductName());
		copyCategory.setPhotos(category.getPhotos());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);

		return copyCategory;
	}

	public static Category copyFull(Category category, String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setProductName(name);

		return copyCategory;
	}

	public Category(String name) {
		this.productName = name;
		this.alias = name;
		this.photos = "default.png";
	}

	public Category(String productName, Category parent) {
		this(productName);
		this.parent = parent;
	}

	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setProductName(category.getProductName());

		return copyCategory;
	}

	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setProductName(name);

		return copyCategory;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public Category(String productName, String alias, boolean enabled) {
		super();
		this.productName = productName;
		this.alias = alias;
		this.enabled = enabled;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	@Transient
	public String getPhotosImagePath() {
		if (id == null || photos == null)
			return "/images/default-user.png";
		return "/categories-photos/" + this.id + "/" + this.photos;
	}

}
