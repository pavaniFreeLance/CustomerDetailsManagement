package com.service.customerdetails.service;

import java.util.List;

import com.service.customerdetails.model.Customer;

/*
 * Interface for customer service
 */
public interface CustomerService {

	/*
	 * find all customers
	 * 
	 * @return list of customers
	 */
	public List<Customer> findAll();

	/*
	 * find customer by id
	 * 
	 * @param id
	 * 
	 * @return Customer
	 */
	public Customer findCustomerById(int customerId);

	/*
	 * find customer by first name and last name
	 * 
	 * @param first name , last name
	 * 
	 * @return list of all customers
	 */
	public List<Customer> findCustomerByFirstNameAndLastName(String firstName, String lastName);

	/*
	 * Save customer
	 * 
	 * @param customer object to save
	 * 
	 * @return customer object saved
	 */
	public Customer save(Customer customer);

	/*
	 * delete customer by ID
	 * 
	 * @param id
	 * 
	 * @return string
	 */
	public void deleteCustomerById(int customerId);

}
