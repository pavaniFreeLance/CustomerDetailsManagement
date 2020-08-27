package com.service.customerdetails.dto;

import javax.validation.constraints.NotNull;

/*
 * Address DTO holding address details of customer
 */
public class AddressDto {

	private int id;

	@NotNull(message = "Provide Street name")
	private String street;

	@NotNull(message = "Provide City name")
	private String city;

	@NotNull(message = "Provide state name")
	private String state;

	@NotNull(message = "Provide zip code")
	private String zipCode;

	@NotNull(message = "Provide country name")
	private String country;
	
	public AddressDto() {
		super();
	}

	/*
	 * All args constructor
	 */
	public AddressDto(int id, String street, String city, String state, String zipCode, String country) {
		this.id = id;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	/*
	 * Constructor overloaded
	 */
	public AddressDto(String street, String city, String state, String zipCode, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	// Getter and setters
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	/*
	 * Overriding toString method
	 */
	@Override
	public String toString() {
		return "Address id:" + id + " street: " + street + " city :" + city + " state: " + state + " zipCode: "
				+ zipCode + " country: " + country;
	}
}
