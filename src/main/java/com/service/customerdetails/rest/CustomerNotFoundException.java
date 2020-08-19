package com.service.customerdetails.rest;

/*
 *  Customer not found Exception thrown when Customer id not found
 *  in the Rest Controller class
 */
public class CustomerNotFoundException extends RuntimeException{

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);	
	}

	public CustomerNotFoundException(String message) {
		super(message);		
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);		
	}

	
	

}
