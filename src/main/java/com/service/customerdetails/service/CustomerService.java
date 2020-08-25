package com.service.customerdetails.service;

import java.util.List;
import java.util.Optional;

import com.service.customerdetails.model.Customer;

/*
 * Interface for customer service
 */
public interface CustomerService {

	/*
	 * find customer by id
	 * 
	 * @param id
	 * 
	 * @return Customer
	 */
	public Optional<Customer> findCustomerById(int customerId);

	/*
	 * find all customers or by first name and/Or last name
	 * 
	 * @param optional first name , optional last name
	 * 
	 * @return list of all customers
	 */
	public Optional<List<Customer>> findCustomers(Optional<String> firstName, Optional<String> lastName);

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
