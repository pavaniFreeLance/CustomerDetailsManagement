package com.service.customerdetails.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.service.customerdetails.rest.ApplicationConstants;

/*
 * Basic Spring security configuration class extending WebSecurityConfigurerAdapter
 */
@Configuration
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * Swagger related URI
	 */
	private static final String[] AUTH_LIST = { "/v2/api-docs", "/configuration/ui", "/swagger-resources",
			"/configuration/security", "/swagger-ui.html", "/webjars/**" };

	/*
	 * Creating two users with roles USER and ADMIN for In Memory authentication
	 * 
	 * @param AuthenticationManagerBuilder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// In memory authentication
		auth.inMemoryAuthentication().withUser("user").password("{noop}psw").roles(ApplicationConstants.ROLE_USER).and().withUser("Admin")
		.password("{noop}psw").roles(ApplicationConstants.ROLE_USER, ApplicationConstants.ROLE_ADMIN);

	}

	/*
	 * Securing the rest end points with HTTP Basic authentication
	 * 
	 * @param HttpSecurity
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().antMatchers(AUTH_LIST).authenticated()
		.antMatchers(HttpMethod.GET, ApplicationConstants.URI_MATCHER).hasAnyRole(ApplicationConstants.ROLE_USER, ApplicationConstants.ROLE_ADMIN).antMatchers(HttpMethod.POST, ApplicationConstants.URI_MATCHER)
		.hasRole(ApplicationConstants.ROLE_ADMIN).antMatchers(HttpMethod.PUT, ApplicationConstants.URI_MATCHER).hasRole(ApplicationConstants.ROLE_ADMIN)
		.antMatchers(HttpMethod.DELETE, ApplicationConstants.URI_MATCHER).hasRole(ApplicationConstants.ROLE_ADMIN).anyRequest().authenticated().and().csrf()
		.disable().formLogin();

	}

}
