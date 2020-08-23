package com.service.customerdetails.rest;

/*
 *  CustomerNotFoundException is a runtime exception thrown 
 *  when Customer not found with the given ID
 *  
 */
public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/*
	 * Overloaded Constructor calling super class constructor passing throwable
	 * object and string as arguments.
	 * 
	 * @param String, Throwable
	 */
	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/*
	 * Overloaded Constructor calling super class constructor passing String as
	 * argument.
	 * 
	 * @param String
	 */
	public CustomerNotFoundException(String message) {
		super(message);
	}

	/*
	 * Overloaded Constructor calling super class constructor passing throwable
	 * object as argument.
	 * 
	 * @param String, Throwable
	 */
	public CustomerNotFoundException(Throwable cause) {
		super(cause);
	}

}
