package com.service.customerdetails.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Exception handler class
 */
@ControllerAdvice
public class RestExceptionHandler {

	/*
	 * Handling Customer not found exception and returning error response
	 * 
	 * @param CustomerNotFoundException
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private ResponseEntity<ErrorResponse> handleException(CustomerNotFoundException exc) {

		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	/*
	 * Handle General Exception
	 * 
	 * @param Exception
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	private ResponseEntity<ErrorResponse> handleException(Exception exc) {

		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage(exc.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * Handle MethodArgumentNotValidException
	 * 
	 * @param MethodArgumentNotValidException
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid2(MethodArgumentNotValidException ex) {

		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ex.getMessage());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		error.setDetails(errors);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

	}

}
