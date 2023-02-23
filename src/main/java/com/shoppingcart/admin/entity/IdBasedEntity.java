package com.shoppingcart.admin.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass//Đẩy các thông tin này cho user và role khi tạo table
public abstract class IdBasedEntity {
	@Id//đại diện cho khóa chính
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Id tự động tăng ~ AUTO_INCREMENT

	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
