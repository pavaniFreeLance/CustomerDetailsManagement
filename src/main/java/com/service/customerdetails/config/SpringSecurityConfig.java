package com.service.customerdetails.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	//Swagger URI
	private static final String[] AUTH_LIST = { 
            "/v2/api-docs", 
            "/configuration/ui", 
            "/swagger-resources",
            "/configuration/security", 
            "/swagger-ui.html", 
            "/webjars/**" 
};
	
	// Create 2 users
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()

				.withUser("user").password("{noop}psw").roles("USER").and()
				.withUser("Admin")
				.password("{noop}psw").roles("USER", "ADMIN");

	}
	
	// Secure the endpoins with HTTP Basic authentication
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				// HTTP Basic authentication
				.httpBasic().and().authorizeRequests()
				.antMatchers(AUTH_LIST).authenticated()
				.antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER,ADMIN")
				.antMatchers(HttpMethod.POST, "/api/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN").and().csrf().disable()
				.formLogin();
	}

}
