package com.service.customerdetails.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Entity class to store Customer details
 */
@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank(message = "first name cannot be blank")
	@Column(name = "first_name")
	private String firstName;

	@NotBlank(message = "last name cannot be blank")
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "age")
	private int age;

	@NotNull(message = "Address should be provided")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "addressid")
	private Address address;

	/*
	 * Default constructor
	 */
	public Customer() {

	}

	/*
	 * All args constructor
	 */
	public Customer(int id, String firstName, String lastName, int age, Address address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.address = address;
	}

	/*
	 * Constructor overloaded
	 */
	public Customer(String firstName, String lastName, int age, Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.address = address;
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

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/*
	 * Overriding toString method
	 */
	@Override
	public String toString() {
		return "Customer id:" + id + " firstName: " + firstName + " lastName :" + lastName + " age: " + age;
	}

}
