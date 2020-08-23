package com.service.customerdetails.rest;

import java.util.List;

/*
 *Error object to return in case of any Exception in restController 
 */
public class ErrorResponse {

	private int status;
	private String message;
	private List<String> details;

	/*
	 * default constructor
	 */
	public ErrorResponse() {

	}

	/*
	 * Overloaded constructor
	 * 
	 * @param status, message
	 */
	public ErrorResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	/*
	 * Overloaded constructor
	 * 
	 * @param status, message,details
	 */
	public ErrorResponse(int status, String message, List<String> details) {
		super();
		this.status = status;
		this.message = message;
		this.details = details;
	}

	// Getters and setters
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

}
