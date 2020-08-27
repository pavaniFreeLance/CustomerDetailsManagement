package com.service.customerdetails.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.customerdetails.model.Customer;
import com.service.customerdetails.repository.CustomerRepository;

/*
 * This class implement CustomerService interface  
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;

	/*
	 * Constructor auto wiring customeRepository
	 * 
	 * @param customerRepository
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	/*
	 * find customer by id
	 * 
	 * @param id
	 * 
	 * @return Customer
	 */
	public Optional<Customer> findCustomerById(int customerId) {

		Optional<Customer> resultCustomer = this.customerRepository.findById(customerId);

		return resultCustomer;

	}

	/*
	 * find Customers by first name and last name (or)
	 * find Customers by first name only (or) last name only (or)
	 * Find all customers in case no first name and last name provided.	 * 
	 * 
	 * @param Optional first name , Optional last name
	 * 
	 * @return Optional list of all customersDto
	 */
	public Optional<List<Customer>> findCustomers(Optional<String> firstName, Optional<String> lastName) {

		Optional<List<Customer>> customersList;

		if (firstName.isPresent() && lastName.isPresent()) {

			customersList = this.customerRepository.findByFirstNameAndLastNameIgnoreCase(firstName.get(),
					lastName.get());

		} else if (firstName.isPresent()) {

			customersList = this.customerRepository.findByFirstName(firstName.get());

		} else if (lastName.isPresent()) {

			customersList = this.customerRepository.findByLastName(lastName.get());

		} else {

			customersList = Optional.ofNullable(this.customerRepository.findAll());
		}

		return customersList;
	}

	/*
	 * Save customer
	 * 
	 * @param customer object to save
	 * 
	 * @return customer object saved
	 */
	public Customer saveCustomer(Customer customer) {

		return this.customerRepository.save(customer);
	}

	/*
	 * delete customer by ID
	 * 
	 * @param id
	 * 
	 * @return string
	 */
	public void deleteCustomerById(int customerId) {
		this.customerRepository.deleteById(customerId);
	}	

}
