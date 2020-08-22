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
public class SwaggerConfig {

	/*Creating Docket*/
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.service.customerdetails.rest")).build()
				.apiInfo(this.metaData());
	}

	/*
	 * Setting Meta data in swagger 
	 */
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("My REST API", "Rest API for Managing Customer Information", "1.0", "Free to use",
				new Contact("Pavani", "URL not availbale", "Pavani.Bandi@gmail.com"), "API Licence",
				"URL not available", Collections.emptyList());
		return apiInfo;
	}
}