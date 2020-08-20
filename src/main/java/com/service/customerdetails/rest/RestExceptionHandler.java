package com.service.customerdetails.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
	
    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class); 
	/*
	  * Handling Customer not found exception and returning error response
	  * @param CustomerNotFoundException	
	  * return ResponseEntity with ErrorResponse object as JSOn    
	  */
    @ExceptionHandler
	private ResponseEntity<ErrorResponse> handleException(CustomerNotFoundException exc) {
		
    	ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		LOGGER.info(" Customer not found exception thrown with status "+error.getStatus());
				
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	 }
    
    /*
     * Handle General Exception
     * @param Exception
     * return ResponseEntity with ErrorResponse object as JSOn
     */ 
    @ExceptionHandler
	private ResponseEntity<ErrorResponse> handleException(Exception exc) {
				
    	ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		LOGGER.info(" Exception thrown with status "+error.getStatus());
			
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
