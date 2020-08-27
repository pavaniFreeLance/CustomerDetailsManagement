package com.service.customerdetails.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * Configuring and enabling Swagger and Swagger UI */
@Configuration
@EnableSwagger2
public class CustomerManagementSwaggerConfig {

	private final String BASE_PACKAGE = "com.service.customerdetails.rest";
	/*Creating Docket*/
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE)).build()
				.apiInfo(this.metaData());
	}

	/*
	 * Setting Meta data in swagger 
	 */
	private ApiInfo metaData() {
		return new ApiInfo("Customer Management", "Rest API for Managing Customer Information", "1.0", "Free to use",
				new Contact("Pavani", "URL", "Pavani.Bandi@gmail.com"), "API Licence",
				"URL", Collections.emptyList());

	}
}