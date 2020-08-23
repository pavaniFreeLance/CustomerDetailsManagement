package com.service.customerdetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CustomerdetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerdetailsApplication.class, args);
	}

}
