package com.shoppingcart.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class webSecurityConfig extends WebSecurityConfigurerAdapter {// Cấu hình webSecurity. Tạo 1 class và kế thừa từ
																		// WebSecurityConfigurerAdapter
	// Nó sẽ hiểu đến cấu hình và phân quyền trong class này
	// Khi khai báo @Bean phải có @Configuration
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShoppingUserDetailsService();
	}

	// TẠo ra spring bean, khi có spring bean thì sẽ có 1 cái khác autowired
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		// BCrypt là một kỹ thuật mã hóa -> trong bài này dùng để mã hóa password
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	// Dưới đây là ghi đè để cấu hình theo ý người lập trình
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/users/**", "/categories/**").hasAuthority("Admin") // phân quyền -> /** là bất kì thứ gì:
																					// /users/edit,
				// /users/save,... phải có role là admin, có thể phân
				// nhiều role như hasAnyRole("","")
				.antMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin", "Editor")

				.anyRequest().authenticated()// Tất cả request đều phải login -> .premitall là không cần phải login
				.and().formLogin().loginPage("/login")// đây là đg dẫn @GetMapping
				.usernameParameter("email")// email này phải bắt đúng bên html name=email, nếu là user name thì ko cần
											// dòng này, nó mặc định lun
				.permitAll().and().rememberMe().key("Abc_123")// restart app no need login again, abc-123 là một ký tự bất kì để mã hóa
				.tokenValiditySeconds(7 * 24 * 60 * 60);
	}

	// Tiếp tục override configure với tham số là websecurity
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
		// dùng để hiển thị các Hình ảnh,... trước khi login
	}
}
