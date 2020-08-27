package com.service.customerdetails.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
 * Exception handler class
 */
@RestController
@Validated
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	/*
	 * Handling Customer not found exception and returning error response
	 * 
	 * @param CustomerNotFoundException
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private ResponseEntity<ErrorResponse> handleException(CustomerNotFoundException ex) {

		return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	/*
	 * Handle MethodArgumentNotValidException
	 * 
	 * @param MethodArgumentNotValidException
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errors), HttpStatus.BAD_REQUEST);

	}

	/*
	 * Handle ConstraintViolationException
	 * 
	 * @param ConstraintViolationException
	 * 
	 * @return ResponseEntity with ErrorResponse object as JSOn
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {

		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + 
					violation.getPropertyPath() + ": " + violation.getMessage());
		}

		return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage(), errors), HttpStatus.BAD_REQUEST);

	}




}
