package com.shoppingcart.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// addResourceHandlers: Thêm trình xử lý để phục vụ các tài nguyên tĩnh như hình ảnh, js và tệp css
		// từ các vị trí cụ thể trong thư mục gốc của ứng dụng web, đường dẫn lớp và các
		// vị trí khác.
		exposeDirectory("user-photos", registry);
		exposeDirectory("categories-photos", registry);
		exposeDirectory("brands-photos", registry);

	}

	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern + "/**"; // tất cả folder trong user photo được phép truy cập hết

		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
	}
}
