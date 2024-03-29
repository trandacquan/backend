package com.shoppingcart.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends IdBasedEntity{
	@Column(length = 255, nullable = false, unique=true)
	private String name;
	
	@Column(length = 255, nullable = false, unique=true)
	private String alias;
	
	@Column(length = 512, nullable = false, name="short_description")
	private String shortDescription;
	
	@Column(length = 4096, nullable = false, name="full_description")
	private String fullDescription;
	
	@Column(length = 64, nullable = false)
	private String mainImage;
	
	@Column(name="created_time", nullable = false, updatable=false)
	private Date createdTime;
	
	@Column(name="updated_time")
	private Date updatedTime;
	
	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;
	

	private float cost;

	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;
	

	private float length;

	private float width;

	private float height;

	private float weight;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	//Relationship between products and brands
	@ManyToOne
	@JoinColumn(name="brand_id")
	private Brand brand;

	public Product() {
		super();
	}

	public Product(String name, String alias, String shortDescription, String fullDescription, String mainImage,
			Date createTime, Date updateTime, boolean enabled, boolean inStock, float cost, float price,
			float discountPercent, float length, float width, float height, float weight, Category category,
			Brand brand) {
		super();
		this.name = name;
		this.alias = alias;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.mainImage = mainImage;
		this.createdTime = createTime;
		this.updatedTime = updateTime;
		this.enabled = enabled;
		this.inStock = inStock;
		this.cost = cost;
		this.price = price;
		this.discountPercent = discountPercent;
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.category = category;
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
}