package com.shoppingcart.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.shoppingcart.admin.*", "com.shoppingcart.admin"})//Quét qua package com.shoppingcart.admin.* -> Các package cần quét qua để khởi tạo spring bean
@EnableJpaRepositories(basePackages = {"com.shoppingcart.admin.*"})//Khởi tạo spring bean -> quét qua package và tìm repository để kích hoạc
@EntityScan({"com.shoppingcart.admin.*"})//Dựa vào cấu hình của các entity để tạo table và column. Quét qua package nếu package nào có @entity thì sẽ khởi tạo class tương ứng
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
