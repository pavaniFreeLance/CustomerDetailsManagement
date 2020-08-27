package com.service.customerdetails.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/*
 * DTO Class to store Customer details
 */

public class CustomerDto {

	private int id;

	@NotBlank(message = "first name cannot be blank")
	private String firstName;

	@NotBlank(message = "last name cannot be blank")	
	private String lastName;

	@Positive(message = "Age should be positive number")
	private int age;

	@Valid
	@NotNull(message = "Address should be provided")
	private AddressDto addressDto;


	public CustomerDto() {
		super();
	}

	/*
	 * All args constructor
	 */
	public CustomerDto(int id, String firstName, String lastName, int age, AddressDto addressDto) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.addressDto = addressDto;
	}

	/*
	 * Constructor overloaded
	 */
	public CustomerDto(String firstName, String lastName, int age, AddressDto addressDto) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.addressDto = addressDto;
	}

	// Getters and Setters

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public AddressDto getAddressDto() {
		return addressDto;
	}

	public void setAddressDto(AddressDto addressDto) {
		this.addressDto = addressDto;
	}

	/*
	 * Overriding toString method
	 */
	@Override
	public String toString() {
		return "Customer id:" + id + " firstName: " + firstName + " lastName :" + lastName + " age: " + age;
	}

}
