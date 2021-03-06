package com.service.customerdetails.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.service.customerdetails.service.CustomerServiceImpl;

/*
 * Class used for logging before Customer Management Rest API called. 
 */
@Aspect
@Component
public class CustomerManagementLogBeforeRestCall {

	private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

	/*
	 * Method is called before every Rest API in the CustomerRestController class.
	 */
	@Before("execution(public * com.service.customerdetails.rest.CustomerRestController.*(..))")
	public void beforeRestCallLogging() {

		LOGGER.info("***Rest api Ivoked ***");

	}

}
